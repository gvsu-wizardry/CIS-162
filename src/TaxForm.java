import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;
/**
 * TaxForm class that calculates the 2014 U.S. federal
 * tax for a single person claimed as a dependent.
 * This simplified form is only for taxpayers who earned
 * less than $100,000 and have no dependents
 *
 * @author Santiago Quiroga
 * @version 11 Octubre 2018
 */
public class TaxForm  {
  /** Money you earned and reported on a W-2 form.  */
  private double wages;

  /** Interest your banks paid you on your saving account */
  private double taxInt;

  /** Government payments for unemployment insurance */
  private double unemployment;

  /** Amount of tax your employer withheld from your paychecks */
  private double withheld;

  /** Adjusted gross income (AGI) */
  private double agi = 0.0;

  /** Taxable income – AGI minus deductions */
  private double taxableIncome = 0.0;

  /** deductions */
  private double deductions;

  /** exceptions */
  private int exceptions;

  /** Tax – amount of tax is based on the taxable income
   *  and rates provided in the tax table */
  private double tax = 0.0;

  /** Refund – if the tax is less than the tax withheld  */
  private double refund = 0.0;

  /** constants used to calculate the federal tax */

  private static final double TEN_PCT = 0.10;
  private static final double FIFTEEN_PCT = 0.15 ;
  private static final double TWENTY_FIVE_PCT = 0.25;
  private static final double TWENTY_EIGHT_PCT = 0.28;

  NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);

  public void estimateTaxes() {

    getData();

    agi = wages +  taxInt + unemployment;
    taxableIncome = agi - deductions;
    if (taxableIncome < 0 )
      taxableIncome = 0;

    //Calculating tax
    if (exceptions != 2) {
      calcSingle();
      print(0);
    }
    else {
      calcMarried();
      print(1);
    }
  }

  private void getData() {

    Scanner scan = new Scanner (System.in);
    System.out.println ("Wages, salaries and tips: ");
    wages = scan.nextDouble();
    System.out.println ("Taxable interest: " );
    taxInt = scan.nextDouble();
    System.out.println ("Unemployment compensation:  " );
    unemployment = scan.nextDouble();
    System.out.println ("Exceptions (0, 1 or 2): " );
    exceptions = scan.nextInt();
    System.out.println ("Federal income tax withheld:   " );
    withheld = scan.nextDouble();

    deductions = calcDeductions(exceptions );
  }

  private void print (int filingStatus){
    // printing your information
    System.out.println ("   Your information  ");
    System.out.println ("************************");
    System.out.println ("Wages, salaries and tips: " + fmt.format(wages));
    System.out.println ("Taxable interest: " + fmt.format (taxInt));
    System.out.println ("Unemployment compensation: " + fmt.format(unemployment));
    System.out.println ("Deductions (0, 1 or 2): "  + deductions);
    System.out.println ("Federal income tax withheld: " + fmt.format(withheld));
    System.out.println ("filing status (single (0) or married filing jointly (1) " +
        filingStatus);

    // printing your results
    System.out.println ("     Your Results       ");
    System.out.println ("************************");
    System.out.println ("AGI: " + fmt.format(agi));
    System.out.println ("Taxable Income: " + fmt.format (taxableIncome));
    System.out.println ("Federal Tax: " + fmt.format(tax));
    if (refund < 0)
      System.out.println ("AmountDue: "  + fmt.format( Math.abs(refund)));
    else
      System.out.println ("Your Refund: "  + (fmt.format (refund)));
  }

  private double calcDeductions (int exceptions)
  {
    double deductions;
    switch (exceptions) {
      case 0:
        deductions = 6350; break;
      case 1:
        deductions = 10400; break;
      case 2:
        deductions = 20800; break;
      default:
        deductions = 0;
    }
    return deductions;
  }

  private void calcSingle () {
    if (taxableIncome > 0 && taxableIncome <= 9325)
      tax = Math.round(taxableIncome * TEN_PCT);
    if (taxableIncome > 9325 && taxableIncome <= 37950)
      tax = Math.round (932.5 + ((taxableIncome - 9325) * FIFTEEN_PCT));
    if (taxableIncome > 37950 && taxableIncome <= 91900)
      tax = Math.round (5226.25 + ((taxableIncome - 37950) * TWENTY_FIVE_PCT));
    if (taxableIncome > 91900)
      tax = Math.round (18713.75 + ((taxableIncome - 91900) * TWENTY_EIGHT_PCT));
    refund = withheld - tax;
  }

  private void calcMarried () {
    if (taxableIncome > 0 && taxableIncome <= 18650)
      tax = Math.round(taxableIncome * TEN_PCT);
    if (taxableIncome > 18650 && taxableIncome <= 75900)
      tax = Math.round (1865 + ((taxableIncome - 18650) * FIFTEEN_PCT));
    if (taxableIncome > 75900)
      tax = Math.round (10452.50 + ((taxableIncome - 75900) * TWENTY_FIVE_PCT));
    refund = withheld - tax;
  }
}
