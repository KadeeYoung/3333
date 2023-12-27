package server;

class CliAndMsg{
    ClientHandler cli;
    String msg;

    public CliAndMsg(ClientHandler cli, String msg) {
        this.cli = cli;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
