import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class TaxForm {

  final double TEN_PERCENT = 0.10;
  final double FIFTEEN_PERCENT = 0.15;
  final double TWENTYFIVE_PERCENT = 0.25;
  final double TWENTYEIGHT_PERCENT = 0.28;

  final double TEN_PERCENT_CAP_TAX_SINGLE = 932.5;
  final double FIFTEEN_PERCENT_CAP_TAX_SINGLE = 4293.75;
  final double TWENTYFIVE_PERCENT_CAP_TAX_SINGLE = 14312.5;

  final int TEN_PERCENT_CAP_SINGLE = 9325;
  final int FIFTEEN_PERCENT_CAP_SINGLE = 37950;
  final int TWENTYFIVE_PERCENT_CAP_SINGLE = 91900;

  final double TEN_PERCENT_CAP_TAX_JOINT = 1865;
  final double FIFTEEN_PERCENT_CAP_TAX_JOINT = 8587.5;

  final int TEN_PERCENT_CAP = 18650;
  final int FIFTEEN_PERCENT_CAP = 75900;

  double wages;
  double taxableInterest;
  double unemploymentCompendation;
  double federalIncomeTaxWithheld;
  double deduction;
  double adjustedGrossIncome;
  double taxableIncome;
  double tax;
  double amountOwed;
  double refund;

  int exemptions;

  public static void main(String[] args) {
      TaxForm taxForm = new TaxForm();
      taxForm.estimateTaxes();
  }

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
    exemptions  = prompt.nextInt();

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
    System.out.println("Amount due: " + format.format(amountOwed));
  }

  // Helper methods
  private void calculateDeduction(){
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

  private void calculateAGI(){
    adjustedGrossIncome = wages + taxableInterest + unemploymentCompendation;
  }

  private void calculateTaxableIncome(){
    taxableIncome = adjustedGrossIncome - deduction;

    if (taxableIncome < 0){
      taxableIncome = 0;
    }
  }

  private void calculateTax(){
    tax = taxableIncome;

    if (exemptions != 0){

      if(exemptions == 1){
        calculateSingleTax();
      }else{
        calculateJointTax();
      }
    }

  }

  @SuppressWarnings("Duplicates")
  private void calculateSingleTax(){
    int iteration = 0;
    double taxableIncome = this.taxableIncome;

    while(iteration < 4 && taxableIncome != 0){
      switch (iteration){
        case 0:
            if(taxableIncome - TEN_PERCENT_CAP_SINGLE <= 0){
              tax = taxableIncome * TEN_PERCENT;
              iteration = 4;
            }else{
              tax = TEN_PERCENT_CAP_SINGLE;
              taxableIncome -= TEN_PERCENT_CAP_SINGLE;
              ++iteration;
            }
          break;
        case 1:
          if(taxableIncome - FIFTEEN_PERCENT_CAP_SINGLE >= 0){
            tax = taxableIncome * FIFTEEN_PERCENT;
            iteration = 4;
          }else{
            tax += taxableIncome * FIFTEEN_PERCENT ;
            taxableIncome -= FIFTEEN_PERCENT_CAP_SINGLE;
            ++iteration;
          }
          break;
        case 2:
          if(taxableIncome - TWENTYFIVE_PERCENT_CAP_SINGLE >= 0){
            tax = taxableIncome * TWENTYFIVE_PERCENT;
            iteration = 4;
          }else{
            tax += taxableIncome * TWENTYFIVE_PERCENT;
            taxableIncome -= TWENTYFIVE_PERCENT_CAP_SINGLE;
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

  private void calculateJointTax(){
      if (taxableIncome <= TEN_PERCENT_CAP_TAX_JOINT){
        tax = taxableIncome * TEN_PERCENT;
      }else if(taxableIncome <= FIFTEEN_PERCENT_CAP_TAX_JOINT){
        tax = (taxableIncome - TEN_PERCENT_CAP_TAX_JOINT ) * FIFTEEN_PERCENT + TEN_PERCENT_CAP_TAX_JOINT;
      }else{
        tax = (taxableIncome - FIFTEEN_PERCENT_CAP_TAX_JOINT  - TEN_PERCENT_CAP_TAX_JOINT) *
            TWENTYFIVE_PERCENT + FIFTEEN_PERCENT_CAP_TAX_JOINT + TEN_PERCENT_CAP_TAX_JOINT;
      }
  }

  private void calculateAmountOwed(){
      if(tax > federalIncomeTaxWithheld) {
        amountOwed = tax - federalIncomeTaxWithheld;
      }else{
        amountOwed = 0;
      }
  }

  private void calculateRefund(){
    if(tax < federalIncomeTaxWithheld) {
      refund = federalIncomeTaxWithheld - tax;
    }else{
      refund = 0;
    }
  }

}
