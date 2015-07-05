package lv.company.edup.infrastructure.utils.amount;

public class LatvianNumberToWords extends NumberToWords {

    LatvianNumberToWords() {
    }

    @Override
    public String[] getTensNames() {
        return new String[]{
                "",
                " desmit",
                " divdesmit",
                " trīsdesmit",
                " četrdesmit",
                " piecdesmit",
                " sešdesmit",
                " septiņdesmit",
                " astoņdesmit",
                " deviņdesmit"
        };
    }

    @Override
    public String[] getNumNames() {
        return new String[] {
                "",
                " viens",
                " divi",
                " trīs",
                " četri",
                " pieci",
                " seši",
                " septiņi",
                " astoņi",
                " deviņi",
                " desmit",
                " vienspadsmit",
                " divpadsmit",
                " trīspadsmit",
                " četrpadsmit",
                " piecpadsmit",
                " sešpadsmit",
                " septiņpadsmit",
                " astoņpadsmit",
                " deviņpadsmit"
        };
    }

    @Override
    protected String translateHundred(int number) {
        return number == 1 ? "simts" : "simti";
    }

    @Override
    public String translateBillion(int number) {
        return number == 1 ? "miliards" : "miliardi";
    }

    @Override
    public String translateMillion(int number) {
        return number == 1 ? "milions" : "milioni";
    }

    @Override
    public String translateThousand(int number) {
        return number == 1 ? "tūkstotis" : "tūkstoši";
    }

    @Override
    public String translateZero() {
        return "nulle";
    }

}
