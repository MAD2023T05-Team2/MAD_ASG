package sg.edu.np.mad.productivibe;

public class User {

    private int userId;
    private String name;
    private String userName;
    private String passWord;

    public User(){}

    public User(int userId, String name, String userName, String passWord) {
        this.userId = userId;
        this.name = name;
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getUserId() {
        return String.valueOf(userId);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
