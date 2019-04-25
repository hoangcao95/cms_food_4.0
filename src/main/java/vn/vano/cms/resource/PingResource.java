package vn.vano.cms.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import vn.vano.cms.common.IGsonBase;
import vn.vano.cms.common.ResponseData;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Component
@Path(value = "/ping")
@Produces(value = { MediaType.APPLICATION_JSON })
@Consumes(value = { MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED })
public class PingResource implements IGsonBase {
    private final Logger LOG = LoggerFactory.getLogger(PingResource.class);

    @Context
    private HttpServletRequest request;
    @Context
    private HttpHeaders httpHeaders;

    @GET
    public ResponseData ping() {
        String ip = null;
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception ex) {

        }
        return ResponseData.buildResponse(ResponseData.SUCCESS, "READY " + ip);
    }
}
