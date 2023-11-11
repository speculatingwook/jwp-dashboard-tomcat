package org.apache.coyote.http11;

public class ResponseDto {
    private final byte[] header;
    private final byte[] data;

    public ResponseDto(String header, byte[] imageData) {
        this.header = header.getBytes();
        this.data = imageData;
    }

    public byte[] getHeader() {
        return header;
    }

    public byte[] getData() {
        return data;
    }
}
