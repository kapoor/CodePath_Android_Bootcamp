import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.dialogtest.R;


public class EditDialog extends DialogFragment {

	private EditText mEditText;

	public EditDialog() {
		// Empty constructor required for DialogFragment
	}
	
	// Note: This is how a constructor for a fragment looks
	public static EditDialog newInstance(String title) {
		EditDialog frag = new EditDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dialog, container);
		mEditText = (EditText) view.findViewById(R.id.txt_your_name);
		String title = getArguments().getString("title", "Enter Name");
		getDialog().setTitle(title);
		// Show soft keyboard automatically
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return view;
	}
		
}
