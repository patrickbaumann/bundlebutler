Fragment Argument Helper
========================

Example
-------

	public class MyFragment extends Fragment {
		@Argument("keyName")
		Double someValue;
		
		@Argument("keyName")
		Thing someP; // extends parcelable
		
		public static MyFragment createInstance(Double someArg, Thing otherArg) {
		    MyFragment fragment = new MyFragment()
		    fragment.someValue = someArg;
		    fragment.someP = otherArg;
		    FragmentArgs.saveArgs(fragment);
		)
		
		onCreate(Bundle savedInstanceState) {
		    FragmentArgs.loadWithState(this, savedInstanceState);
		}
		
		onSaveInstanceState(Bundle state) {
		    FragmentArgs.saveState(this);
		}
	}
