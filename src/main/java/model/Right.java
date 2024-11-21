package model;

public class Right {
    private Long id;
    private String right;

    public Right(Long id, String right){
        this.id = id;
        this.right = right;
    }

    public Long getId() {
        return id;
    }

    public String getRight() {
        return right;
    }
}
