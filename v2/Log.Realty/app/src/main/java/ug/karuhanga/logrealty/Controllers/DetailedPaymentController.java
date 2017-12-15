package ug.karuhanga.logrealty.Controllers;

import com.orm.query.Select;

import ug.karuhanga.logrealty.Models.Payment;
import ug.karuhanga.logrealty.R;
import ug.karuhanga.logrealty.Views.Confirmation;
import ug.karuhanga.logrealty.Views.DetailedPayment;

import static ug.karuhanga.logrealty.Helper.REQUEST_CODE_DELETE;
import static ug.karuhanga.logrealty.Helper.dateToString;
import static ug.karuhanga.logrealty.Helper.toCurrency;

/**
 * Created by karuhanga on 12/14/17.
 */

public class DetailedPaymentController implements DetailedPayment.DetailedPaymentActivityExternalInterface, Confirmation.ConfirmationExternalInterface {

    private Payment payment;
    private Controller.DetailedPaymentControllerExternalInterface dashboard;

    public DetailedPaymentController(Controller.DetailedPaymentControllerExternalInterface dashboard) {
        this.dashboard= dashboard;
    }

    @Override
    public void setPayment(long id) {
        try {
            this.payment= Payment.findById(Payment.class, id);
        }catch (Exception e){
            this.payment= Select.from(Payment.class).limit("1").first();
        }
        if (this.payment==null){
            this.payment= Select.from(Payment.class).limit("1").first();
        }
    }

    @Override
    public String getTenant() {
        return payment.getTenant().getName();
    }

    @Override
    public void deletePayment() {
        new Confirmation(dashboard.requestContext(), this, "Are you sure?", "Delete this payment:\n"+payment.summarize(), R.drawable.ic_delete_black_24dp, "Yes", "Cancel", REQUEST_CODE_DELETE).show();
    }

    @Override
    public String getAmount() {
        return toCurrency(payment.getAmount());
    }

    @Override
    public String getDate() {
        return dateToString(payment.getDate());
    }

    @Override
    public String getRate() {
        return toCurrency(payment.getRate());
    }

    @Override
    public void onReceiveResult(boolean result, int requestCode) {
        if (result && requestCode==REQUEST_CODE_DELETE){
            long id= payment.getId();
            payment.delete();
            dashboard.onPaymentDeleted(id);
        }
    }
}
