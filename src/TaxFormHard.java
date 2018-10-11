import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;


/*************************************************************************************************
 * TaxFormHard class that calculates the 2014 U.S. federal
 * tax for a single person claimed as a dependent.
 * This simplified form is only for taxpayers who earned
 * less than $100,000 and have no dependents
 *
 * @author Santiago Quiroga
 * @version 11 October 2018
 ************************************************************************************************/
public class TaxFormHard {

  /**
   * Variable description
   */
  private final double TEN_PERCENT = 0.10;
  /**
   * Variable description
   */
  private final double FIFTEEN_PERCENT = 0.15;
  /**
   * Variable description
   */
  private final double TWENTYFIVE_PERCENT = 0.25;
  /**
   * Variable description
   */
  private final double TWENTYEIGHT_PERCENT = 0.28;
  /**
   * Variable description
   */

  private final double TEN_PERCENT_TAX_CAP_SINGLE = 932.5;
  /**
   * Variable description
   */
  private final double FIFTEEN_PERCENT_TAX_CAP_SINGLE = 4293.75;
  /**
   * Variable description
   */
  private final double TWENTYFIVE_PERCENT_TAX_CAP_SINGLE = 13487.5;
  /**
   * Variable description
   */

  private final int TEN_PERCENT_CAP_SINGLE = 9325;
  /**
   * Variable description
   */
  private final int FIFTEEN_PERCENT_CAP_SINGLE = 37950;
  /**
   * Variable description
   */
  private final int TWENTYFIVE_PERCENT_CAP_SINGLE = 91900;
  /**
   * Variable description
   */

  private final double TEN_PERCENT_CAP_TAX_JOINT = 1865;
  /**
   * Variable description
   */
  private final double FIFTEEN_PERCENT_CAP_TAX_JOINT = 8587.5;
  /**
   * Variable description
   */

  private final int TEN_PERCENT_CAP = 18650;
  /**
   * Variable description
   */
  private final int FIFTEEN_PERCENT_CAP = 75900;
  /**
   * Variable description
   */

  private double wages;
  /**
   * Variable description
   */
  private double taxableInterest;
  /**
   * Variable description
   */
  private double unemploymentCompendation;
  /**
   * Variable description
   */
  private double federalIncomeTaxWithheld;
  /**
   * Variable description
   */
  private double deduction;
  /**
   * Variable description
   */
  private double adjustedGrossIncome;
  /**
   * Variable description
   */
  private double taxableIncome;
  /**
   * Variable description
   */
  private double tax;
  /**
   * Variable description
   */
  private double amountOwed;
  /**
   * Variable description
   */
  private double refund;
  /**
   * Variable description
   */

  int exemptions;
  /** Variable description */

  /*************************************************************************************************
   * This methods instantiates all the variables to their and set their value to zero.
   ************************************************************************************************/
  public TaxFormHard() {
    wages = 0;
    taxableInterest = 0;
    unemploymentCompendation = 0;
    federalIncomeTaxWithheld = 0;
    deduction = 0;
    adjustedGrossIncome = 0;
    taxableIncome = 0;
    tax = 0;
    amountOwed = 0;
    refund = 0;
    exemptions = 0;
  }

  /*************************************************************************************************
   * This method will prompt the user for 4 different inputs and then will calculate the
   * user's taxes based on the specific inputs.
   ************************************************************************************************/
  public void estimateTaxes() {

    askUserForInputs();

    calculateDeduction();
    calculateAGI();
    calculateTaxableIncome();
    calculateTax();
    calculateAmountOwed();
    calculateRefund();

    printResults();

  }

  // Helper methods

  /*************************************************************************************************
   * This method will use a scanner to obtain the required inputs from the user.
   ************************************************************************************************/
  private void askUserForInputs(){

    Scanner prompt = new Scanner(System.in);
    System.out.println("Your Information");

    System.out.print("Wages, salaries and tips: ");
    wages = (double) prompt.nextInt();

    System.out.print("Taxable interest: ");
    taxableInterest = (double) prompt.nextInt();

    System.out.print("Unemployment compensation: ");
    unemploymentCompendation = (double) prompt.nextInt();
    unemploymentCompendation = 0;

    System.out.print("Exemptions (0, 1 or 2): ");
    exemptions = prompt.nextInt();

    System.out.print("Federal income tax withheld: ");
    federalIncomeTaxWithheld = (double) prompt.nextInt();

  }


  /*************************************************************************************************
   * This method will calculate deduction according to the specific exemptions.
   ************************************************************************************************/
  private void calculateDeduction() {
    switch (exemptions) {
      case 0:
        deduction = 6350;
        break;
      case 1:
        deduction = 10400;
        break;
      case 2:
        deduction = 20800;
        break;
      default:
        deduction = 0;
    }
  }

  /*************************************************************************************************
   * This method will calculate AGI, the total of wages, taxable interest and unemployment
   ************************************************************************************************/
  private void calculateAGI() {
    adjustedGrossIncome = wages + taxableInterest + unemploymentCompendation;
  }

  /*************************************************************************************************
   *  AGI minus deductions (can not be below zero)
   ************************************************************************************************/
  private void calculateTaxableIncome() {
    taxableIncome = adjustedGrossIncome - deduction;

    if (taxableIncome < 0) {
      taxableIncome = 0;
    }
  }

  /*************************************************************************************************
   *  Amount of tax is based on the taxable income and rates provided in the tax table depending on
   *  filing status (e.g. single or married filing jointly).
   ************************************************************************************************/
  private void calculateTax() {

    if (exemptions == 1 || exemptions == 0) {

      if (exemptions == 1) {
        calculateSingleTax();
      } else {
        calculateJointTax();
      }
    }else {
      tax = 0;
    }

  }

  /*************************************************************************************************
   * Calculates tax for the singles bracket (0 or 1 exemptions). Furthermore, this method uses a
   * loop to calculate the specific tax by taking the specific tax on each individual portion
   * The main benefit being that the 4 different tax brackets are based on the given constants, so
   * the method could be easily adjusted when tax brackets changed.
   ************************************************************************************************/
  @SuppressWarnings("Duplicates")
  private void calculateSingleTax() {
    double taxableIncome = this.taxableIncome;

    for (int iteration = 0; iteration < 4 && taxableIncome != 0; ++ iteration) {
      switch (iteration) {
        case 0:
          if (taxableIncome - TEN_PERCENT_CAP_SINGLE <= 0) {
            tax = taxableIncome * TEN_PERCENT;
            iteration = 4;
          } else {
            tax = TEN_PERCENT_TAX_CAP_SINGLE ;
            taxableIncome -= TEN_PERCENT_CAP_SINGLE;
          }
          break;
        case 1:
          if (taxableIncome - FIFTEEN_PERCENT_CAP_SINGLE <= 0) {
            tax += taxableIncome * FIFTEEN_PERCENT;
            iteration = 4;
          } else {
            tax += FIFTEEN_PERCENT_TAX_CAP_SINGLE;
            taxableIncome -= FIFTEEN_PERCENT_CAP_SINGLE;
          }
          break;
        case 2:https://www.priorityhealth.com/
          if (taxableIncome - TWENTYFIVE_PERCENT_CAP_SINGLE <= 0) {
            tax += taxableIncome * TWENTYFIVE_PERCENT;
            iteration = 4;
          } else {
            tax += TWENTYFIVE_PERCENT_TAX_CAP_SINGLE;
            taxableIncome -= taxableIncome - TWENTYFIVE_PERCENT_CAP_SINGLE;
          }
          break;
        case 3:
          tax += taxableIncome * TWENTYEIGHT_PERCENT;
          break;
      }
    }
  }

  /*************************************************************************************************
   * This method calculates joint tax (exemption 2)
   ************************************************************************************************/
  private void calculateJointTax() {

  if (taxableIncome <= TEN_PERCENT_CAP) {
      tax = taxableIncome * TEN_PERCENT;
    } else if (taxableIncome <= FIFTEEN_PERCENT_CAP) {
      tax =
          (taxableIncome - TEN_PERCENT_CAP_TAX_JOINT) * FIFTEEN_PERCENT + TEN_PERCENT_CAP_TAX_JOINT;
    } else {
      tax = (taxableIncome - FIFTEEN_PERCENT_CAP_TAX_JOINT - TEN_PERCENT_CAP_TAX_JOINT) *
          TWENTYFIVE_PERCENT + FIFTEEN_PERCENT_CAP_TAX_JOINT + TEN_PERCENT_CAP_TAX_JOINT;
    }
  }

  /*************************************************************************************************
   * If the tax is greater than the tax withheld
   ************************************************************************************************/
  private void calculateAmountOwed() {
    if (tax > federalIncomeTaxWithheld) {
      amountOwed = tax - federalIncomeTaxWithheld;
    } else {
      amountOwed = 0;
    }
  }

  /*************************************************************************************************
   * If the tax is less than the tax withheld
   ************************************************************************************************/
  private void calculateRefund() {
    if (tax < federalIncomeTaxWithheld) {
      refund = federalIncomeTaxWithheld - tax;
    } else {
      refund = 0;
    }
  }

  /*************************************************************************************************
   * This method will print the results to the screen.
   ************************************************************************************************/
  private void printResults() {
    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);

    System.out.println();
    System.out.println("Your Results: ");

    System.out.println("AGI: " + formatter.format(adjustedGrossIncome));
    System.out.println("Taxable income: " + formatter.format(taxableIncome));
    System.out.println("Federal tax: " + formatter.format(tax));

    if (amountOwed > refund) {
      System.out.println("Amount due: " + formatter.format(amountOwed));
    } else {
      System.out.println("Your refund: " + formatter.format(refund));
    }
  }

  /*************************************************************************************************
   * The main method will run the  estimate taxes method.
   * @param args
   ************************************************************************************************/
 public static void main(String[] args) {
    TaxFormHard form = new TaxFormHard();
    form.estimateTaxes();
  }
}
