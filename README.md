Fragment Args
========================
FragmentArgs is an Android library that binds fields of a fragment to bundle entries for use in creating / reading from an arguments bundle and populating / reading from a saved instance state bundle.

Fields annotated with @Argument will be treated as both arguments and instance state. The string defined in the annotation is the key that will be used when the value is stored into the bundle.

The library generates the boilerplate you would otherwise have to write to manage saved instance state and arguments.

* Call `FragmentArgs.saveArgs(fragment)` on your fragment after setting the fields appropriately to set the arguments bundle on the fragment.
* Call `FragmentArgs.saveState(this, instanceState)` on your fragment when saving instance state to store all of your arguments in the state bundle you pass in as the second param.
* Call `FragmentArgs.loadWithState(this, savedInstanceState)` to load the arguments followed by the saved instance state into your fields. This will result in any saved instance state overriding the original arguments.

Example
-------
```java
public class MyFragment extends Fragment {
	@Argument("thing_id")
	long thingId = -1L;
	
	@Argument("thing_object")
	Thing thing; // implements Parcelable
	
	@Argument("last_updated")
	Date date = new Date(); // implements Serializable
	
	public static MyFragment createInstance(long id, Thing thing) {
	    MyFragment fragment = new MyFragment()
	    fragment.thingId = id;
	    fragment.thing = thing;
	    FragmentArgs.saveArgs(fragment);
	)
	
	onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    FragmentArgs.loadWithState(this, savedInstanceState);
	}
	
	onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(Bundle state);
	    FragmentArgs.saveState(this, state);
	}
}
```