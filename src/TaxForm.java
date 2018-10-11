import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;


/*************************************************************************************************
 * @author Santiago Quiroga
 * @version 11 October 2018
 ************************************************************************************************/
public class TaxForm {

  /**
   * Variable description
   */
  final double TEN_PERCENT = 0.10;
  /**
   * Variable description
   */
  final double FIFTEEN_PERCENT = 0.15;
  /**
   * Variable description
   */
  final double TWENTYFIVE_PERCENT = 0.25;
  /**
   * Variable description
   */
  final double TWENTYEIGHT_PERCENT = 0.28;
  /**
   * Variable description
   */

  final double TEN_PERCENT_TAX_CAP_SINGLE = 932.5;
  /**
   * Variable description
   */
  final double FIFTEEN_PERCENT_TAX_CAP_SINGLE = 4293.75;
  /**
   * Variable description
   */
  final double TWENTYFIVE_PERCENT_TAX_CAP_SINGLE = 14312.5;
  /**
   * Variable description
   */

  final int TEN_PERCENT_CAP_SINGLE = 9325;
  /**
   * Variable description
   */
  final int FIFTEEN_PERCENT_CAP_SINGLE = 37950;
  /**
   * Variable description
   */
  final int TWENTYFIVE_PERCENT_CAP_SINGLE = 91900;
  /**
   * Variable description
   */

  final double TEN_PERCENT_CAP_TAX_JOINT = 1865;
  /**
   * Variable description
   */
  final double FIFTEEN_PERCENT_CAP_TAX_JOINT = 8587.5;
  /**
   * Variable description
   */

  final int TEN_PERCENT_CAP = 18650;
  /**
   * Variable description
   */
  final int FIFTEEN_PERCENT_CAP = 75900;
  /**
   * Variable description
   */

  double wages;
  /**
   * Variable description
   */
  double taxableInterest;
  /**
   * Variable description
   */
  double unemploymentCompendation;
  /**
   * Variable description
   */
  double federalIncomeTaxWithheld;
  /**
   * Variable description
   */
  double deduction;
  /**
   * Variable description
   */
  double adjustedGrossIncome;
  /**
   * Variable description
   */
  double taxableIncome;
  /**
   * Variable description
   */
  double tax;
  /**
   * Variable description
   */
  double amountOwed;
  /**
   * Variable description
   */
  double refund;
  /**
   * Variable description
   */

  int exemptions;
  /** Variable description */

  /*************************************************************************************************
   *
   * @param args
   ************************************************************************************************/
  public static void main(String[] args) {
    TaxForm taxForm = new TaxForm();
    taxForm.estimateTaxes();
  }

  /*************************************************************************************************
   *
   ************************************************************************************************/
  public void estimateTaxes() {
    Scanner prompt = new Scanner(System.in);
    NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

    System.out.println("Your Information");

    System.out.print("Wages, salaries and tips: ");
    wages = (double) prompt.nextInt();

    System.out.print("Taxable interest: ");
    taxableInterest = (double) prompt.nextInt();

    System.out.print("Unemployment compensation: ");
    unemploymentCompendation = (double) prompt.nextInt();

    System.out.print("Exemptions (0, 1 or 2): ");
    exemptions = prompt.nextInt();

    System.out.print("Federal income tax withheld: ");
    federalIncomeTaxWithheld = (double) prompt.nextInt();

    calculateDeduction();
    calculateAGI();
    calculateTaxableIncome();
    calculateTax();
    calculateAmountOwed();
    calculateRefund();

    System.out.println();
    System.out.println("Your Results: ");

    System.out.println("AGI: " + format.format(adjustedGrossIncome));
    System.out.println("Taxable income: " + format.format(taxableIncome));
    System.out.println("Federal tax: " + format.format(tax));

    if (amountOwed > refund) {
      System.out.println("Amount due: " + format.format(amountOwed));
    } else {
      System.out.println("Your refund: " + format.format(amountOwed));
    }

  }

  // Helper methods

  /*************************************************************************************************
   *
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
   *
   ************************************************************************************************/
  private void calculateAGI() {
    adjustedGrossIncome = wages + taxableInterest + unemploymentCompendation;
  }

  /*************************************************************************************************
   *
   ************************************************************************************************/
  private void calculateTaxableIncome() {
    taxableIncome = adjustedGrossIncome - deduction;

    if (taxableIncome < 0) {
      taxableIncome = 0;
    }
  }

  /*************************************************************************************************
   *
   ************************************************************************************************/
  private void calculateTax() {
    tax = taxableIncome;

    if (exemptions != 0) {

      if (exemptions == 1) {
        calculateSingleTax();
      } else {
        calculateJointTax();
      }
    }else {
      tax = adjustedGrossIncome;
    }

  }

  /*************************************************************************************************
   *
   ************************************************************************************************/
  @SuppressWarnings("Duplicates")
  private void calculateSingleTax() {
    int iteration = 0;
    double taxableIncome = this.taxableIncome;

    while (iteration < 4 && taxableIncome != 0) {
      switch (iteration) {
        case 0:
          if (taxableIncome - TEN_PERCENT_CAP_SINGLE <= 0) {
            tax = taxableIncome * TEN_PERCENT;
            iteration = 4;
          } else {
            tax = TEN_PERCENT_CAP_SINGLE;
            taxableIncome -= TEN_PERCENT_TAX_CAP_SINGLE;
            ++iteration;
          }
          break;
        case 1:
          if (taxableIncome - FIFTEEN_PERCENT_CAP_SINGLE <= 0) {
            tax += taxableIncome * FIFTEEN_PERCENT;
            iteration = 4;
          } else {
            tax += FIFTEEN_PERCENT_TAX_CAP_SINGLE;
            taxableIncome -= FIFTEEN_PERCENT_CAP_SINGLE;
            ++iteration;
          }
          break;
        case 2:
          if (taxableIncome - TWENTYFIVE_PERCENT_CAP_SINGLE <= 0) {
            tax += taxableIncome * TWENTYFIVE_PERCENT;
            iteration = 4;
          } else {
            tax += TWENTYFIVE_PERCENT_TAX_CAP_SINGLE;
            taxableIncome -= taxableIncome - TWENTYFIVE_PERCENT_CAP_SINGLE;
            ++iteration;
          }
          break;
        case 3:
          tax += taxableIncome * TWENTYEIGHT_PERCENT;
          ++iteration;
          break;
      }
    }
  }

  /*************************************************************************************************
   *
   ************************************************************************************************/
  private void calculateJointTax() {
    if (taxableIncome <= TEN_PERCENT_CAP_TAX_JOINT) {
      tax = taxableIncome * TEN_PERCENT;
    } else if (taxableIncome <= FIFTEEN_PERCENT_CAP_TAX_JOINT) {
      tax =
          (taxableIncome - TEN_PERCENT_CAP_TAX_JOINT) * FIFTEEN_PERCENT + TEN_PERCENT_CAP_TAX_JOINT;
    } else {
      tax = (taxableIncome - FIFTEEN_PERCENT_CAP_TAX_JOINT - TEN_PERCENT_CAP_TAX_JOINT) *
          TWENTYFIVE_PERCENT + FIFTEEN_PERCENT_CAP_TAX_JOINT + TEN_PERCENT_CAP_TAX_JOINT;
    }
  }

  /*************************************************************************************************
   *
   ************************************************************************************************/
  private void calculateAmountOwed() {
    if (tax > federalIncomeTaxWithheld) {
      amountOwed = tax - federalIncomeTaxWithheld;
    } else {
      amountOwed = 0;
    }
  }

  /*************************************************************************************************
   *
   ************************************************************************************************/
  private void calculateRefund() {
    if (tax < federalIncomeTaxWithheld) {
      refund = federalIncomeTaxWithheld - tax;
    } else {
      refund = 0;
    }
  }

}
