package com.telloquent.vms.servicemanager;

import com.telloquent.vms.model.additionalvisitors.AdditionalVrfResponse;
import com.telloquent.vms.model.authenticate.AuthenticateRequest;
import com.telloquent.vms.model.authenticate.AuthenticateResponse;
import com.telloquent.vms.model.checkvisitors.VisitorDetails;
import com.telloquent.vms.model.checkvisitors.VisitorDetailsResponse;
import com.telloquent.vms.model.createvrf.CreateVrfDetailsResponse;
import com.telloquent.vms.model.licensekeymodel.LicenseDetails;
import com.telloquent.vms.model.licensekeymodel.LicenseDetailsData;
import com.telloquent.vms.model.otp.OtpDetails;
import com.telloquent.vms.model.otp.OtpDetailsResponse;
import com.telloquent.vms.model.settingsmodel.SettingDetailsResponse;
import com.telloquent.vms.model.visitorlist.VisitiorListResponse;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by Telloquent-DM6M on 9/8/2017.
 */

public interface NetworkService {
    final static String baseURL = ServiceManager.kWebServicesVersion + "api/v1";

    @POST(baseURL + "/validate_license")
    Observable<LicenseDetails> getClassResponse(@Body LicenseDetailsData licenseDetailsData);

    @GET(baseURL + "/location_details")
    Observable<SettingDetailsResponse> getSettingDetailsByTokenId(@Query("token") String tokenId);

    @POST(baseURL + "/visitor_details")
    Observable<VisitorDetailsResponse> visitorDetailsServiceResponse(@Query("token") String tokenId, @Body VisitorDetails checkVisitors);

    @POST(baseURL + "/otp")
    Observable<OtpDetailsResponse> otpServiceResponse(@Query("token") String tokenId, @Body OtpDetails otpDetails);

    @Multipart
    @POST(baseURL + "/create_vrf")
    Observable<CreateVrfDetailsResponse> createVrfServiceResponse(@Query("token") String tokenId,@Part MultipartBody.Part email,@Part MultipartBody.Part phone,@Part MultipartBody.Part name,@Part MultipartBody.Part locality, @Part MultipartBody.Part city,
                                                                  @Part MultipartBody.Part state,@Part MultipartBody.Part country,  @Part MultipartBody.Part pin_code,@Part MultipartBody.Part vrf_type,@Part MultipartBody.Part employeeEmail,@Part MultipartBody.Part employeephone,
                                                                  @Part MultipartBody.Part purpose,@Part MultipartBody.Part duration,@Part MultipartBody.Part department,@Part MultipartBody.Part company,@Part MultipartBody.Part file);
    @GET(baseURL + "/view_visitors")
    Observable<VisitiorListResponse> viewVisitorsList(@Query("token") String tokenId, @Query("vrfid") String vRfId);

    @POST(baseURL + "/authenticate_visitor")
    Observable<AuthenticateResponse> authenticateServiceResponse(@Query("token") String tokenId, @Body AuthenticateRequest authenticateRequest);

    @Multipart
    @POST(baseURL + "/submit_vrf")
    Observable<AdditionalVrfResponse> additionalNoOfVrf(@Query("token") String tokenId, @Part MultipartBody.Part file,@Part MultipartBody.Part vrfsid,@Part MultipartBody.Part additionalVisitors);


    @Multipart
    @POST(baseURL + "/additional_visitor")
    Observable<AdditionalVrfResponse> additionalVisitorsDetail(@Query("token") String tokenId,@Part MultipartBody.Part vrfid,
                @Part MultipartBody.Part email,@Part MultipartBody.Part phone,@Part MultipartBody.Part name,@Part MultipartBody.Part company,@Part MultipartBody.Part locality,@Part MultipartBody.Part city,
                @Part MultipartBody.Part state,@Part MultipartBody.Part country,@Part MultipartBody.Part pin_code,@Part MultipartBody.Part file);

}
