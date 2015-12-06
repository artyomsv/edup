package lv.company.edup.infrastructure.filters;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class ResetableHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] rawData;
    private HttpServletRequest request;
    private ResetableServletInputStream servletStream;

    public ResetableHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    public void resetInputStream() {
        servletStream = new ResetableServletInputStream(rawData);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(request.getInputStream());
            servletStream = new ResetableServletInputStream(rawData);
        }
        return servletStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(request.getReader());
            servletStream = new ResetableServletInputStream(rawData);
        }
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(rawData)));
    }

    public class ResetableServletInputStream extends ServletInputStream {

        private ByteArrayInputStream buffer;

        public ResetableServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents != null ? contents : new byte[0]);
        }

        @Override
        public int read() throws IOException {
            return buffer.read();
        }

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }
        public void setReadListener(ReadListener readListener) {
        }

    }



}