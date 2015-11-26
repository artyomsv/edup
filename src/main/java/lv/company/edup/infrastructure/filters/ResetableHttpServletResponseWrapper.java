package lv.company.edup.infrastructure.filters;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ResetableHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream rawData;
    private ServletOutputStream servletOutputStream;

    public ResetableHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        rawData = new ByteArrayOutputStream();
        try {
            final ServletOutputStream responseOutputStream = response.getOutputStream();

            servletOutputStream = new ServletOutputStream() {

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override
                public void write(int b) throws IOException {
                    rawData.write(b);
                    responseOutputStream.write(b);
                }
            };

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return servletOutputStream;
    }

    public String getRawData() {
        return rawData.toString();
    }
}
