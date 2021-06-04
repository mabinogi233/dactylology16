package com.king.dactylology.ResourceOperator.Dao.entity;

public class resource {
    private Integer id;

    private String type;

    private String word;

    private String filepathpic;

    private String filepathmovie;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word == null ? null : word.trim();
    }

    public String getFilepathpic() {
        return filepathpic;
    }

    public void setFilepathpic(String filepathpic) {
        this.filepathpic = filepathpic == null ? null : filepathpic.trim();
    }

    public String getFilepathmovie() {
        return filepathmovie;
    }

    public void setFilepathmovie(String filepathmovie) {
        this.filepathmovie = filepathmovie == null ? null : filepathmovie.trim();
    }
}