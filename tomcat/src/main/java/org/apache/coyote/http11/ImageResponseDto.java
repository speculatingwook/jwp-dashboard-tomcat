package org.apache.coyote.http11;

public class ImageResponseDto {
    private final String header;
    private final byte[] imageData;

    public ImageResponseDto(String header, byte[] imageData) {
        this.header = header;
        this.imageData = imageData;
    }

    public String getHeader() {
        return header;
    }
    public byte[] getImageData(){
        return imageData;
    }
}
