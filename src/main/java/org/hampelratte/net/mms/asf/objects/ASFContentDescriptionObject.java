package org.hampelratte.net.mms.asf.objects;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.hampelratte.net.mms.asf.UnknownAsfObjectException;
import org.hampelratte.net.mms.asf.io.ASFInputStream;

public class ASFContentDescriptionObject extends ASFHeaderObject {

    private String title;

    private String author;

    private String copyright;

    private String description;

    private String rating;

    @Override
    public void setData(byte[] data) throws IOException, UnknownAsfObjectException, InstantiationException, IllegalAccessException {
        super.setData(data);

        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        ASFInputStream asfin = new ASFInputStream(bin);

        // read the length of the strings. subtract the terminating NULL-byte
        int titleLength = asfin.readLEShort();
        int authorLength = asfin.readLEShort();
        int copyrightLength = asfin.readLEShort();
        int descriptionLength = asfin.readLEShort();
        int ratingLength = asfin.readLEShort();

        title = asfin.readUTF16LE(titleLength);
        author = asfin.readUTF16LE(authorLength);
        copyright = asfin.readUTF16LE(copyrightLength);
        description = asfin.readUTF16LE(descriptionLength);
        rating = asfin.readUTF16LE(ratingLength);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" [Title:");
        sb.append(getTitle());
        sb.append(",Author:");
        sb.append(getAuthor());
        sb.append(",Copyright:");
        sb.append(getCopyright());
        sb.append(",Description:");
        sb.append(getDescription());
        sb.append(",Rating:");
        sb.append(getRating());
        sb.append(']');
        return sb.toString();
    }

}
