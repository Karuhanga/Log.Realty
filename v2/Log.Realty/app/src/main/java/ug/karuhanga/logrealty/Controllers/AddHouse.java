package ug.karuhanga.logrealty.Controllers;

import android.content.Intent;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

import java.util.List;

import ug.karuhanga.logrealty.Data.House;
import ug.karuhanga.logrealty.Data.Location;
import ug.karuhanga.logrealty.Helpers;

/**
 * Created by karuhanga on 12/2/17.
 */

public class AddHouse implements ug.karuhanga.logrealty.Viewers.Activities.AddHouse.Controller {
    Location chosen;
    Slave slave;

    public AddHouse(AddHouse.Slave slave){
        this.slave= slave;
    }


    @Override
    public List<Location> requestLocationData() {
        return Select.from(Location.class).list();
    }

    @Override
    public void onSubmit(boolean singleHouse, int houseNumber, String description, boolean defaultRent, long rentPaid){
        //Must have a location
        if (chosen==null){
            slave.onSubmissionError("Location", "Please pick a house location or add one first");
            return;
        }

        //Only one single house
        List<House> results= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(0)).list();
        if (!(results.isEmpty())){
            onError(editTextNumber, "This house was already added");
            return;
        }

        //No description and house number
        if (checkBoxSingleHouse.isChecked()){

            results= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).list();
            if (!(results.isEmpty())){
                onError(editTextNumber, "This house was already added");
                return;
            }

            //No rent
            if (checkBoxDefaultRent.isChecked()){
                house= new House(chosen);
                house.save();
                Intent finisher= new Intent();
                finisher.putExtra("details", house.toString());
                finish();
                return;
            }

            //else get rent paid
            try{
                rentPaid= Integer.valueOf(editTextRentPaid.getText().toString());
            }catch (NumberFormatException e){
                onError(editTextRentPaid, "Please input a rent amount");
                return;
            }
            if (rentPaid< Helpers.AMOUNT_MINIMUM_RENT){
                onError(editTextRentPaid, "Rent must be at least UgShs.250,000/=");
                return;
            }
            house= new House(chosen, rentPaid);
            house.save();
            Intent finisher= new Intent();
            finisher.putExtra("details", house.toString());
            finish();
            return;
        }

        desc= description.getText().toString();
        if (desc.length()<1){
            onError(description, "Please provide a small description");
            return;
        }

        try{
            number= Integer.valueOf(editTextNumber.getText().toString());
        }catch (NumberFormatException e){
            onError(editTextNumber, "Please input a house number");
            return;
        }
        List<House> responses= Select.from(House.class).where(Condition.prop(NamingHelper.toSQLNameDefault("location")).eq(chosen)).and(Condition.prop(NamingHelper.toSQLNameDefault("number")).eq(number)).list();
        if (!(responses.isEmpty())){
            onError(editTextNumber, "This house was added already");
            return;
        }
        if (number==0){
            onError(editTextNumber, "Please input a valid house number");
            return;
        }

        if (checkBoxDefaultRent.isChecked()){
            house= new House(number, desc, chosen);
            house.save();
            Intent finisher= new Intent();
            finisher.putExtra("details", house.toString());
            finish();
            return;
        }

        try{
            rentPaid= Integer.valueOf(editTextRentPaid.getText().toString());
        }catch (NumberFormatException e){
            onError(editTextRentPaid, "Please input a rent amount");
            return;
        }
        if (rentPaid< Helpers.AMOUNT_MINIMUM_RENT){
            onError(editTextRentPaid, "Rent must be at least UgShs.250,000/=");
            return;
        }

        house= new House(number,desc, chosen, rentPaid);
        house.save();
        Intent finisher= new Intent();
        finisher.putExtra("details", house.toString());
        finish();
    }

    public interface Slave{
        void onLocationError(String message);
    }
}
