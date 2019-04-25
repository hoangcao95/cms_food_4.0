package vn.vano.cms.jpa;

import vn.vano.cms.annotation.AntColumn;
import vn.vano.cms.annotation.AntTable;

import java.io.Serializable;

@AntTable(name = "core_sms_syntax", key = "id")
public class CoreSmsSyntax implements Serializable {
    private Long id;
    private String syntax;
    private String regex;
    private String command;
    private String createdDate;
    private String note;
    private Integer status;

    public CoreSmsSyntax() {

    }

    public CoreSmsSyntax(Long id, String syntax, String regex, String command, String createdDate, String note, Integer status) {
        this.id = id;
        this.syntax = syntax;
        this.regex = regex;
        this.command = command;
        this.createdDate = createdDate;
        this.note = note;
        this.status = status;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public Long getId() {
        return id;
    }

    @AntColumn(name = "id", auto_increment = true, index = 0)
    public void setId(Long id) {
        this.id = id;
    }

    @AntColumn(name = "syntax", index = 1)
    public String getSyntax() {
        return syntax;
    }

    @AntColumn(name = "syntax", index = 1)
    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    @AntColumn(name = "regex", index = 2)
    public String getRegex() {
        return regex;
    }

    @AntColumn(name = "regex", index = 2)
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @AntColumn(name = "command", index = 3)
    public String getCommand() {
        return command;
    }

    @AntColumn(name = "command", index = 3)
    public void setCommand(String command) {
        this.command = command;
    }

    @AntColumn(name = "created_date", index = 4)
    public String getCreatedDate() {
        return createdDate;
    }

    @AntColumn(name = "created_date", index = 4)
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @AntColumn(name = "note", index = 5)
    public String getNote() {
        return note;
    }

    @AntColumn(name = "note", index = 5)
    public void setNote(String note) {
        this.note = note;
    }

    @AntColumn(name = "status", index = 6)
    public Integer getStatus() {
        return status;
    }

    @AntColumn(name = "status", index = 6)
    public void setStatus(Integer status) {
        this.status = status;
    }
}
