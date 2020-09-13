package com.mj1666.providers3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.ListTodosQuery;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.w3c.dom.Text;

import javax.annotation.Nonnull;

public class profile extends AppCompatActivity {
TextView  name,address,jobs,rating,phone;
ImageButton phonenumber;

    String[] profiledata;
    String alldata,number;
    private AWSAppSyncClient mAWSAppSyncClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile1);
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
        mAWSAppSyncClient.query(ListTodosQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(todosCallback);
        name=findViewById(R.id.textView2);
        address=findViewById(R.id.textView3);
        jobs=findViewById(R.id.textView4);
        rating=findViewById(R.id.textView5);

    }
    private GraphQLCall.Callback<ListTodosQuery.Data> todosCallback = new GraphQLCall.Callback<ListTodosQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListTodosQuery.Data> response) {
           if(  response.data().listTodos().items().equals(AWSMobileClient.getInstance().getIdentityId()))
           {

               name.setText(response.data().listTodos().__typename());
           }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };
}
