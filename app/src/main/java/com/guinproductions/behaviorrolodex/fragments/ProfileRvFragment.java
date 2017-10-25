package com.guinproductions.behaviorrolodex.fragments;

/**
 * Created by guinp on 10/21/2017.
 */

//public class ProfileRvFragment extends Fragment {
//
//    private static final String TAG = "ProfileListFragment";
//
//    private DatabaseReference mDatabase;
//    Profile profile;
//
//    private FirebaseRecyclerAdapter<Profile, ProfileRV> mAdapter;
//    private RecyclerView mRecycler;
//    private LinearLayoutManager mManager;
//
//    public ProfileRvFragment(){}
//
//
//    @Override
//    public View onCreateView (LayoutInflater inflater, ViewGroup container,
//                              Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View rootView = inflater.inflate(R.layout.content_main, container, false);
//
//        // [START create_database_reference]
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        mRecycler = (RecyclerView) rootView.findViewById(R.id.profile_list);
//        mRecycler.setHasFixedSize(true);
//
//        return rootView;
//    }
//
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        // Set up Layout Manager, reverse layout
//        mManager = new LinearLayoutManager(getActivity());
//        mManager.setReverseLayout(true);
//        mManager.setStackFromEnd(true);
//        mRecycler.setLayoutManager(mManager);
//
//        // Set up FirebaseRecyclerAdapter with the Query
//        Query profilesQuery = getQuery(mDatabase);
//
//        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Profile>()
//                .setQuery(profilesQuery, Profile.class)
//                .build();
//
//        mAdapter = new FirebaseRecyclerAdapter<Profile, ProfileRV>(options) {
//
//            @Override
//            public ProfileRV onCreateViewHolder(ViewGroup viewGroup, int i) {
//                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//                return new ProfileRV(inflater.inflate(R.layout.profile_card, viewGroup, false));
//            }
//
//            @Override
//            protected void onBindViewHolder(ProfileRV viewHolder, int position, final Profile profiles) {
//                final DatabaseReference profileRef = getRef(position);
//
//                // Set click listener for the whole post view
//                final String profileID = profileRef.getKey();
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Launch PostDetailActivity
//                        Intent intent = new Intent(getActivity(), ProfileView.class);
//                        intent.putExtra(ProfileView.EXTRA_PROFILE_KEY, profileID);
//                        startActivity(intent);
//                    }
//                });
//
////                viewHolder.bindToProfile(profiles, new View.OnClickListener(){
////                    @Override
////                    public void onClick(View v){
////                        DatabaseReference globalRef = mDatabase.child("profiles").child(profileRef.getKey());
////                        DatabaseReference userRef = mDatabase.child(profile.uid).child(profileRef.getKey());
////                    }
////                });
//            }
//        };
//        mRecycler.setAdapter(mAdapter);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (mAdapter != null) {
//            mAdapter.startListening();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAdapter != null) {
//            mAdapter.stopListening();
//        }
//    }
//
//
//    //    String profileId = profile.getUid (mDatabase.child("profiles"));
//    public abstract Query getQuery(DatabaseReference databaseReference);
//}



//PROFILERV.CLASS
//
//public class ProfileRV extends RecyclerView.ViewHolder{
//
//    public TextView fnameView;
//    public TextView lnameView;
//    public ImageView imageView;
//
//    public ProfileRV(View itemView) {
//        super(itemView);
//
//        fnameView = (TextView) itemView.findViewById(R.id.view_fname);
//        lnameView = (TextView) itemView.findViewById(R.id.view_lname);
//        imageView = (ImageView) itemView.findViewById(R.id.view_image);
//
//    }
//
//    public void bindToProfile(Profile profile){
//        fnameView.setText(profile.fname);
//        lnameView.setText(profile.lname);
//        imageView.setImageURI(Uri.parse(profile.imageview));
//    }
//}