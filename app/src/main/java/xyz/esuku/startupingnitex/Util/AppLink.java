package xyz.esuku.startupingnitex.Util;

import retrofit2.Call;
import xyz.esuku.startupingnitex.Model.BusinessListingModel;

import java.util.List;

public interface AppLink {


    Call<List<List<BusinessListingModel>>> get_business();
}
