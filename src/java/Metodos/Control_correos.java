package Metodos;

import Controladores.CorreoJpaController;
import Controladores.OrdenTrabajoJpaController;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Control_correos {

    CorreoJpaController jpaccro = new CorreoJpaController();
    OrdenTrabajoJpaController jpacotb = new OrdenTrabajoJpaController();

    public void Correo_ot(int iot, String fco, String uss) throws javax.mail.MessagingException {
        try {
            List lst_correo = jpaccro.Correo_funcion(fco);
            if (lst_correo == null) {
            } else {
                Object[] obj_correos = (Object[]) lst_correo.get(0);
                List lst_verifiar_ot = jpacotb.Verificar_tipo_ot(iot);
                Object[] obj_verificar_ot = (Object[]) lst_verifiar_ot.get(0);
                int id_tipo_mtto = Integer.parseInt(obj_verificar_ot[1].toString());
                List lst_orden_tabajo = null;
                if (id_tipo_mtto <= 2) {
                    lst_orden_tabajo = jpacotb.Orden_trabajo_equipo_id(iot);
                } else if (id_tipo_mtto == 3) {
                    lst_orden_tabajo = jpacotb.Orden_trabajo_zona_id(iot);
                } else {
                    lst_orden_tabajo = jpacotb.Orden_trabajo_general_id(iot);
                }
                Object[] obj_orden_trabajo = (Object[]) lst_orden_tabajo.get(0);
                String origen = ((id_tipo_mtto <= 2) ? obj_orden_trabajo[20].toString() : obj_orden_trabajo[22].toString());
                Properties propiedades = new Properties();
                propiedades.setProperty("mail.smtp.host", "" + obj_correos[6].toString() + "");
                propiedades.setProperty("mail.smtp.starttls.enable", "true");
                propiedades.setProperty("mail.smtp.port", "" + obj_correos[7].toString() + "");//465...587
                propiedades.setProperty("mail.smtp.auth", "true");
                propiedades.setProperty("mail.smtp.socketFactory.port", "587");
                propiedades.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                propiedades.setProperty("mail.smtp.socketFactory.fallback", "true");
                propiedades.setProperty("mail.smtp.user", "" + obj_correos[2].toString() + "");
                Session session = Session.getDefaultInstance(propiedades);
                try {
                    MimeMessage message = new MimeMessage(session);
                    String[] destino = obj_correos[4].toString().split(",");
                    InternetAddress[] addresto = new InternetAddress[destino.length];
                    for (int i = 0; i < destino.length; i++) {
                        addresto[i] = new InternetAddress(destino[i]);
                    }
                    message.setRecipients(Message.RecipientType.TO, addresto);// correo destinatario
                    message.setFrom(new InternetAddress("aplicativo@plastitec-sa.com"));
                    message.setSubject("" + obj_correos[1].toString() + " # " + obj_orden_trabajo[1] + " / " + origen);// Asunto
                    String texto_mail = "<div align='center' style='width:100%;background-color:#ddd'>";
                    texto_mail = texto_mail + "<table style=\"width:700px;border:1px solid #205478;font-family:Helvetica, Arial,'Lucida Grande', sans-serif\">";
                    texto_mail = texto_mail + "<tr>";
                    texto_mail = texto_mail + "<td colspan='2' align='center' style='padding:20px;background-color:#4e73df;vertical-align:top;'><b style='color:#fff;font-size:24px'>OT # " + obj_orden_trabajo[1] + "</b><br /><b style='color:#205478;font-size:18px'>" + origen + "</b></td>";
                    texto_mail = texto_mail + "</tr>";
                    texto_mail = texto_mail + "<tr>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff;width:50%'><b style='color:#205478'>Datos OT</b></td>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'><b style='color:#205478'>Responsables</b></td>";
                    texto_mail = texto_mail + "</tr>";
                    texto_mail = texto_mail + "<tr>";
                    if (id_tipo_mtto <= 2) {
                        texto_mail = texto_mail + "<td valign='top' style=';background-color:#fff;padding:10px;border-rigth:1px solid #ddd'><b style='color:#4e73df'>Semana : </b>" + obj_orden_trabajo[19] + "<br/><b style='color:#4e73df'>Tipo Mtto : </b>" + obj_orden_trabajo[18] + "<br/><b style='color:#4e73df'>Linea : </b>" + obj_orden_trabajo[24] + "<br/><b style='color:#4e73df'>Tipo Equipo : </b>" + obj_orden_trabajo[27] + "</td>";
                        texto_mail = texto_mail + "<td valign='top' style=';background-color:#fff;padding:10px;'><b style='color:#4e73df'>Emisor : </b>" + obj_orden_trabajo[4] + "<br/><b style='color:#4e73df'>Ejecutor : </b>" + obj_orden_trabajo[6] + "<br/><b style='color:#4e73df'>Verifica : </b>" + obj_orden_trabajo[8] + "</td>";
                    } else if (id_tipo_mtto == 3) {
                        texto_mail = texto_mail + "<td valign='top' style=';background-color:#fff;padding:10px;border-rigth:1px solid #ddd'><b style='color:#4e73df'>Semana : </b>" + obj_orden_trabajo[19] + "<br/><b style='color:#4e73df'>Tipo Mtto : </b>" + obj_orden_trabajo[18] + "<br/><b style='color:#4e73df'>Linea(s) : </b><br />" + obj_orden_trabajo[23] + "</td>";
                        texto_mail = texto_mail + "<td valign='top' style=';background-color:#fff;padding:10px;'><b style='color:#4e73df'>Emisor : </b>" + obj_orden_trabajo[4] + "<br/><b style='color:#4e73df'>Ejecutor : </b>" + obj_orden_trabajo[6] + "<br/><b style='color:#4e73df'>Revisor : </b>" + obj_orden_trabajo[8] + "<br/><b style='color:#4e73df'>Aprobador : </b>" + obj_orden_trabajo[20] + "</td>";
                    } else {
                        texto_mail = texto_mail + "<td valign='top' style=';background-color:#fff;padding:10px;border-rigth:1px solid #ddd'><b style='color:#4e73df'>Semana : </b>" + obj_orden_trabajo[19] + "<br/><b style='color:#4e73df'>Tipo Mtto : </b>" + obj_orden_trabajo[18] + "<br/><b style='color:#4e73df'>Descripción : </b><br />" + obj_orden_trabajo[23] + "</td>";
                        texto_mail = texto_mail + "<td valign='top' style=';background-color:#fff;padding:10px'><b style='color:#4e73df'>Emisor : </b>" + obj_orden_trabajo[4] + "<br/><b style='color:#4e73df'>Ejecutor : </b>" + obj_orden_trabajo[6] + "<br/><b style='color:#4e73df'>Revisor : </b>" + obj_orden_trabajo[8] + "<br/><b style='color:#4e73df'>Aprobador : </b>" + obj_orden_trabajo[20] + "</td>";
                    }
                    texto_mail = texto_mail + "</tr>";
                    texto_mail = texto_mail + "<tr>";
                    texto_mail = texto_mail + "<td colspan='2' style='border-top:2px solid #34495e;background-color:#fff'>"
                            + "<b style='color: #34495e;'>Coordialmente</b>"
                            + "<p style='color: #34495e;font-weight:normal;'>" + uss + "<br /><b>PMP MTF PLASTITEC</b></p>"
                            + "</td>";
                    texto_mail = texto_mail + "</tr>";
                    texto_mail = texto_mail + "<tr>";
                    texto_mail = texto_mail + "<td colspan='2' style='background-color:#ddd;color: #34495e;font-size:10px;'>La Informacion contenida en este mensaje puede ser confidencial y solo puede ser utilizada por la persona u organizacion a la cual esta dirigida. Si usted no es el receptor "
                            + "autorizado, cualquier retencion, difusion, distribucion o copia de este mensaje es prohibida y sancionada por la ley. Si por error "
                            + "recibe este mensaje, le agradecemos reenviarlo al remitente y borrar el mensaje recibido inmediatamente. PLASTITEC S.A.S, sus subsidiarios y/o empleados no son responsables "
                            + "por la transmision incorrecta o incompleta de este correo electronico o cualquiera de sus adjuntos, ni responsable por cualquier retraso en su recepcion.</td>";
                    texto_mail = texto_mail + "</tr>";
                    texto_mail = texto_mail + "</table>";
                    texto_mail = texto_mail + "</div>";
                    message.setText(texto_mail, "ISO-8859-1", "HTML");//Mensaje
                    Transport transport = session.getTransport("smtp");
                    transport.connect("" + obj_correos[2].toString() + "", "" + obj_correos[3].toString() + "");// Su Correo y Contraseña
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Correo_semanal_ot(String fco, String uss) throws javax.mail.MessagingException {
        try {
            List lst_correo = jpaccro.Correo_funcion(fco);
            List lst_orden_tabajo = jpacotb.Reporte_semanal_ot();
            if (lst_correo == null && lst_orden_tabajo == null) {
            } else {
                Object[] obj_correos = (Object[]) lst_correo.get(0);
                Properties propiedades = new Properties();
                propiedades.setProperty("mail.smtp.host", "" + obj_correos[6].toString() + "");
                propiedades.setProperty("mail.smtp.starttls.enable", "true");
                propiedades.setProperty("mail.smtp.port", "" + obj_correos[7].toString() + "");//465...587
                propiedades.setProperty("mail.smtp.auth", "true");
                propiedades.setProperty("mail.smtp.user", "" + obj_correos[2].toString() + "");
                Session session = Session.getDefaultInstance(propiedades);
                try {
                    MimeMessage message = new MimeMessage(session);
                    String[] destino = obj_correos[4].toString().split(",");
                    InternetAddress[] addresto = new InternetAddress[destino.length];
                    for (int i = 0; i < destino.length; i++) {
                        addresto[i] = new InternetAddress(destino[i]);
                    }
                    message.setRecipients(Message.RecipientType.TO, addresto);// correo destinatario
                    message.setFrom(new InternetAddress("aplicativo@plastitec-sa.com"));
                    message.setSubject("Reporte OT abiertas semanal");// Asunto
                    String texto_mail = "<div align='center' style='width:100%;background-color:#ddd'>";
                    texto_mail = texto_mail + "<table style=\"width:900px;border:1px solid #205478;font-family:Helvetica, Arial,'Lucida Grande', sans-serif\">";
                    texto_mail = texto_mail + "<tr>";
                    texto_mail = texto_mail + "<td colspan='9' align='center' style='padding:20px;background-color:#4e73df;vertical-align:top;'><b style='color:#fff;font-size:24px'>Ordenes de trabajo abiertas</b></td>";
                    texto_mail = texto_mail + "</tr>";
                    texto_mail = texto_mail + "<tr>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff;'><b style='color:#205478'>No. OT</b></td>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff;'><b style='color:#205478'>Fecha</b></td>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'><b style='color:#205478'>Equipo/ Zona /Actividad</b></td>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'><b style='color:#205478'>Tipo Mtto</b></td>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'><b style='color:#205478'>Estado</b></td>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'><b style='color:#205478'>Programador</b></td>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'><b style='color:#205478'>Ejecutor</b></td>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'><b style='color:#205478'>Verificador</b></td>";
                    texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'><b style='color:#205478'>Aprobador</b></td>";
                    texto_mail = texto_mail + "</tr>";
                    for (int i = 0; i < lst_orden_tabajo.size(); i++) {
                        Object[] obj_orden_trabajo = (Object[]) lst_orden_tabajo.get(i);
                        texto_mail = texto_mail + "<tr>";
                        texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'>" + obj_orden_trabajo[1] + "</td>";
                        texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'>" + obj_orden_trabajo[2] + "</td>";
                        texto_mail = texto_mail + "<td style='padding:5px;background-color:#fff'>" + obj_orden_trabajo[5] + "</td>";
                        texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'>" + obj_orden_trabajo[4] + "</td>";
                        texto_mail = texto_mail + "<td align='center' style='padding:5px;background-color:#fff'>" + obj_orden_trabajo[6] + "</td>";
                        texto_mail = texto_mail + "<td style='padding:5px;background-color:#fff'>" + obj_orden_trabajo[7] + "</td>";
                        texto_mail = texto_mail + "<td style='padding:5px;background-color:#fff'>" + obj_orden_trabajo[8] + "</td>";
                        texto_mail = texto_mail + "<td style='padding:5px;background-color:#fff'>" + obj_orden_trabajo[9] + "</td>";
                        texto_mail = texto_mail + "<td style='padding:5px;background-color:#fff'>" + obj_orden_trabajo[10] + "</td>";
                        texto_mail = texto_mail + "</tr>";
                    }
                    texto_mail = texto_mail + "<tr>";
                    texto_mail = texto_mail + "<td colspan='9' style='border-top:2px solid #34495e;background-color:#fff'>"
                            + "<b style='color: #34495e;'>Coordialmente</b>"
                            + "<p style='color: #34495e;font-weight:normal;'>" + uss + "<br /><b>PMP MTF PLASTITEC</b></p>"
                            + "</td>";
                    texto_mail = texto_mail + "</tr>";
                    texto_mail = texto_mail + "<tr>";
                    texto_mail = texto_mail + "<td colspan='9' style='background-color:#ddd;color: #34495e;font-size:10px;'>La Informacion contenida en este mensaje puede ser confidencial y solo puede ser utilizada por la persona u organizacion a la cual esta dirigida. Si usted no es el receptor "
                            + "autorizado, cualquier retencion, difusion, distribucion o copia de este mensaje es prohibida y sancionada por la ley. Si por error "
                            + "recibe este mensaje, le agradecemos reenviarlo al remitente y borrar el mensaje recibido inmediatamente. PLASTITEC S.A.S, sus subsidiarios y/o empleados no son responsables "
                            + "por la transmision incorrecta o incompleta de este correo electronico o cualquiera de sus adjuntos, ni responsable por cualquier retraso en su recepcion.</td>";
                    texto_mail = texto_mail + "</tr>";
                    texto_mail = texto_mail + "</table>";
                    texto_mail = texto_mail + "</div>";
                    message.setText(texto_mail, "ISO-8859-1", "HTML");//Mensaje
                    Transport transport = session.getTransport("smtp");
                    try {
                        transport.connect(obj_correos[2].toString(), obj_correos[3].toString());// Su Correo y Contraseña
                    } catch (Exception e) {
                    }
                    try {
                        transport.sendMessage(message, message.getAllRecipients());
                    } finally {
                        transport.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
