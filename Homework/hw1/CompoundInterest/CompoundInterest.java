/** Collection of compound-interest and related financial
 *  computations.
 *  @author P. N. Hilfinger, Zoe Plaxco
 */
public class CompoundInterest {
    /** Current year. */
    static final int THIS_YEAR = 2020;

    /** Return the number of years between TARGETYEAR and THIS_YEAR,
     *  e.g. if THIS_YEAR is 2020 and TARGETYEAR is 2021, the result
     *  should be 1. Throughout the assignment it is OK to assume that
     *  TARGETYEAR is >= THIS_YEAR. */
    static int numYears(int targetYear){
        return targetYear - THIS_YEAR;
    }

    /** Suppose we have an asset worth PRESENTVALUE that appreciates
     *  by RATE compounded annually. This method returns the value of that
     *  asset in the year given by TARGETYEAR.
     *
     *  RATE is given as a percentage return. For example, if
     *  PRESENTVALUE is 10, the rate is 12, and the TARGETYEAR is 2022,
     *  then the futureValue will be 10*1.12*1.12 = 12.544. */
    static double futureValue(double presentValue, double rate, int targetYear) {
        for(int i = 0; i < numYears(targetYear); i++){
           presentValue *= (1 + rate/100);
        }
        return presentValue;
    }

    /** Returns returns the value, in THIS_YEAR dollars, of an asset
     *  worth PRESENTVALUE that appreciates by RATE compounded
     *  annually in TARGETYEAR, assuming a simple model where inflation
     *  compounds annually at a constant rate of INFLATIONRATE.
     *
     *  For example, suppose PRESENTVALUE is 10, RATE is 12,
     *  TARGETYEAR is 2022, and INFLATIONRATE is 3.
     *  In this case, the nominal value is 12.544. If we convert this into
     *  2020 dollars, we get 12.544 * 0.97 * 0.97 = 11.8026496 dollars. */
    static double futureValueReal(double presentValue, double rate, int targetYear, double inflationRate){
        double nominal = futureValue(presentValue, rate, targetYear);
        for(int i = 0; i < numYears(targetYear); i++){
            nominal *= (1 - inflationRate/100);
        }
        return nominal;
    }

    /** Suppose you invest PERYEAR dollars at the end of every year until
     *  TARGETYEAR, with growth compounded annually at RATE. This method
     *  returns the total value of your savings in TARGETYEAR.
     *
     *  For example, if PERYEAR is 5000, TARGETYEAR is 2022, and RATE is 10,
     *  then the result will be 5000*1.1*1.1 + 5000*1.1 + 5000 =
     *  16550. */
    static double totalSavings(double perYear, int targetYear, double rate){
        int total = 0;
        for (int i = 0; i <= numYears(targetYear); i++){
            total += futureValue(perYear, rate, targetYear-i);
        }
        return total;
    }

    /** Returns totalSavings(PERYEAR, TARGETYEAR, RATE) converted to
     *  current year dollars, assuming a uniform inflation rate of
     *  INFLATIONRATE. */
    static double totalSavingsReal(double perYear, int targetYear, double rate, double inflationRate){
        double nominal = totalSavings(perYear, targetYear, rate);
        for(int i = 0; i < numYears(targetYear); i++){
            nominal *= (1 - inflationRate/100) ;
        }
        return nominal;
    }

    /** Prints out the future inflation-adjusted value of a dollar in
     *  TARGETYEAR in a nice format, assuming yearly compounded
     *  interest at a rate of RETURNRATE, with annual inflation at
     *  INFLATIONRATE. */
    static void printDollarFV(int targetYear, double returnRate, double inflationRate) {
        double nominalDollarValue = futureValue(1,returnRate, targetYear); // replace 0 with your code
        double realDollarValue = futureValueReal(1, returnRate, targetYear, inflationRate);    // replace 0 with your code
        // Do not change anything in this method below this line
        String dollarSummary =
            String.format("Assuming a %.2f%% rate of return,"
                          + " a dollar saved today would be worth"
                          + " %.2f dollars in the year %d, or %.2f dollars"
                          + " adjusted for inflation.", returnRate,
                          nominalDollarValue, targetYear, realDollarValue);
        System.out.println(dollarSummary);
    }

    /** Prints out the future inflation-adjusted value of saving
     *  PERYEAR dollars per year at RETURNRATE until TARGETYEAR at
     *  INFLATIONRATE in a nice format. */
    static void printSavingsFV(int targetYear, double returnRate, double inflationRate, double perYear) {
        double nominalSavings = totalSavings(perYear, targetYear, returnRate); // replace 0 with your code
        double realSavings = totalSavingsReal(perYear, targetYear, returnRate, inflationRate);    // replace 0 with your code
        // Do not change anything in this method below this line
        String savingsSummary =
            String.format("Assuming a %.2f%% rate of return,"
                          + " in the year %d, after saving %.2f"
                          + " dollars per year, you'll have %.2f dollars or"
                          + " %.2f dollars adjusted for inflation.",
                          returnRate, targetYear, perYear,
                          nominalSavings, realSavings);
        System.out.println(savingsSummary);
    }

    /* Parameters for the analysis. */
    /** The year of interest. */
    static final int TARGET_YEAR = 2059;
    /** RETURN_RATE is the percentage rate that you expect to earn on
     *            average until targetYear.
     *  INFLATION_RATE is the average inflation rate until targetYear
     *  PER_YEAR is how much money you will save per year until targetYear */

    static final double RETURN_RATE = 10,
        INFLATION_RATE = 3,
        PER_YEAR = 10000;

    /** Print out future values for given parameters. */
    public static void main(String[] ignored) {
        printDollarFV(TARGET_YEAR, RETURN_RATE, INFLATION_RATE);
        System.out.println();
        printSavingsFV(TARGET_YEAR, RETURN_RATE, INFLATION_RATE, PER_YEAR);
    }
}
