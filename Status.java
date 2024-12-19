class Status {
    private int id;
    private String name;
    private int code;
    private String description;

    public Status(int id, String name, int code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}