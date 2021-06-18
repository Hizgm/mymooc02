package com.imooc.pojo.vo;

public class UsersVO {
    /**
     * ����id �û�id
     */
    private String id;

    /**
     * �û��� �û���
     */
    private String username;

    /**
     * �ǳ� �ǳ�
     */
    private String nickname;

    /**
     * ͷ�� ͷ��
     */
    private String face;

    /**
     * �Ա� �Ա� 1:��  0:Ů  2:����
     */
    private Integer sex;

    // �û��Ựtoken
    private String userUniqueToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getUserUniqueToken() {
        return userUniqueToken;
    }

    public void setUserUniqueToken(String userUniqueToken) {
        this.userUniqueToken = userUniqueToken;
    }
}