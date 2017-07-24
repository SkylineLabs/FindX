package in.skylinelabs.digiPune.activity;

        import android.content.Context;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.widget.ArrayAdapter;

public class CustomAutoCompleteTextChangedListener1 implements TextWatcher{

    public static final String TAG = "CustomAutoCompleteTextChangedListener1.java";
    Context context;

    public CustomAutoCompleteTextChangedListener1(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        Bus_Fetch mainActivity = ((Bus_Fetch) context);
        // query the database based on the user input
        mainActivity.item = mainActivity.getItemsFromDb(userInput.toString());

        mainActivity.myAdapter.notifyDataSetChanged();
        mainActivity.myAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_dropdown_item_1line, mainActivity.item);
        mainActivity.myAutoComplete.setAdapter(mainActivity.myAdapter);

    }

}