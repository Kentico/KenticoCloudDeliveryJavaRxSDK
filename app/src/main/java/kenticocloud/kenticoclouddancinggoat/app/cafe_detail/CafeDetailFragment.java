package kenticocloud.kenticoclouddancinggoat.app.cafe_detail;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import kenticocloud.kenticoclouddancinggoat.R;
import kenticocloud.kenticoclouddancinggoat.app.core.BaseFragment;
import kenticocloud.kenticoclouddancinggoat.app.shared.ScrollChildSwipeRefreshLayout;
import kenticocloud.kenticoclouddancinggoat.data.models.Cafe;
import kenticocloud.kenticoclouddancinggoat.util.Location.LocationHelper;
import kenticocloud.kenticoclouddancinggoat.util.Location.LocationInfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by RichardS on 15. 8. 2017.
 */

public class CafeDetailFragment extends BaseFragment<CafeDetailContract.Presenter> implements CafeDetailContract.View, OnMapReadyCallback  {

    private GoogleMap _map;

    public CafeDetailFragment() {
        // Requires empty public constructor
    }

    public static CafeDetailFragment newInstance() {
        return new CafeDetailFragment();
    }

    @Override
    protected int getFragmentId(){
        return R.layout.cafe_detail_frag;
    }

    @Override
    protected int getViewId(){
        return R.id.cafeDetailRL;
    }

    @Override
    protected boolean hasScrollSwipeRefresh() {
        return true;
    }

    @Override
    protected void onScrollSwipeRefresh() {
        _presenter.loadCafe();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // init map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return _root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void showCafe(Cafe cafe) {
        View view = getView();

        if (view == null) {
            return;
        }

        // Update activity title
        getActivity().setTitle(cafe.getCity());

        // image
        final ImageView teaserIV = (ImageView) view.findViewById(R.id.cafeDetailTeaserIV);
        Picasso.with(view.getContext()).load(cafe.getPhotoUrl()).into(teaserIV);

        TextView streetLineTV = (TextView) view.findViewById(R.id.cafeStreetLineTV);
        streetLineTV.setText(cafe.getStreet());

        TextView cityLineTV = (TextView) view.findViewById(R.id.cafeCityLineTV);
        cityLineTV.setText(cafe.getZipCode() + ", " + cafe.getCity());

        TextView cafeCountryLineTV = (TextView) view.findViewById(R.id.cafeCountryLineTV);
        if (TextUtils.isEmpty(cafe.getState())) {
            cafeCountryLineTV.setText(cafe.getCountry());
        } else {
            cafeCountryLineTV.setText(cafe.getCountry() + ", " + cafe.getState());
        }

        TextView cafePhoneTV = (TextView) view.findViewById(R.id.cafeDetailPhoneTV);
        cafePhoneTV.setText(cafe.getPhone());

        TextView cafeEmailTV = (TextView) view.findViewById(R.id.cafeDetailEmailTV);
        cafeEmailTV.setText(cafe.getEmail());

        // init marker
        LocationInfo cafeLocation = null;
        try {
            cafeLocation = LocationHelper.getLocationFromAddress(getContext(), cafe.getStreet(), cafe.getCity(), cafe.getCountry());

            if (cafeLocation != null){
                LatLng cafeLatLng = new LatLng(cafeLocation.getLattitude(), cafeLocation.getLongtitude());
                _map.addMarker(new MarkerOptions().position(cafeLatLng).title("Cafe"));
                _map.moveCamera(CameraUpdateFactory.newLatLngZoom(cafeLatLng, 12));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLoadingIndicator(false);
        _fragmentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        _map = googleMap;

        _map.getUiSettings().setZoomGesturesEnabled(true);
        // scroll not enabled because it does not play nice with scroll that is required
        // it also causes issues when map takes full screen
        _map.getUiSettings().setScrollGesturesEnabled(false);

        _map.getUiSettings().setMapToolbarEnabled(true);
    }
}
