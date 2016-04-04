package app.turn.out.outturn;


public class OutTurn {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setNfcId(int nfc) {
        this.nfcIds = nfc;
    }

    public int getNfcId() {
        return nfcIds;
    }

    private int id;
    private String date;
    private float weight;
    private int nfcIds;

    private int sId;
    private int sNumBag;
    private int wNumBag;


    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public int getsNumBag() {
        return sNumBag;
    }

    public void setsNumBag(int sNumBag) {
        this.sNumBag = sNumBag;
    }

    public int getwNumBag() {
        return wNumBag;
    }

    public void setwNumBag(int wNumBag) {
        this.wNumBag = wNumBag;
    }



}
