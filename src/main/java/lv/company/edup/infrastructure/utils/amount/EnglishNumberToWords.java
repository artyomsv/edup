package lv.company.edup.infrastructure.utils.amount;

public class EnglishNumberToWords extends NumberToWords {

    EnglishNumberToWords() {
    }

    @Override
    public String[] getTensNames() {
        return new String[]{
                "",
                " ten",
                " twenty",
                " thirty",
                " forty",
                " fifty",
                " sixty",
                " seventy",
                " eighty",
                " ninety"
        };
    }

    @Override
    public String[] getNumNames() {
        return new String[] {
                "",
                " one",
                " two",
                " three",
                " four",
                " five",
                " six",
                " seven",
                " eight",
                " nine",
                " ten",
                " eleven",
                " twelve",
                " thirteen",
                " fourteen",
                " fifteen",
                " sixteen",
                " seventeen",
                " eighteen",
                " nineteen"
        };
    }

    @Override
    protected String translateHundred(int number) {
        return "hundred";
    }

    @Override
    public String translateBillion(int number) {
        return "billion";
    }

    @Override
    public String translateMillion(int number) {
        return "million";
    }

    @Override
    public String translateThousand(int number) {
        return "thousand";
    }

    @Override
    public String translateZero() {
        return "zero";
    }
}
