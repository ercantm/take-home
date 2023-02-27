package fun;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServiceResponse {
    private int id;
    private String text;

    @Override
    public String toString() {
        return "ServiceResponse{" + "\n"+
                "\tid=" + id + ",\n"+
                "\ttext='" + text + '\'' +
                "\n}";
    }
