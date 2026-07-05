/*
 * Copyright (c) The CarToolForge Authors.
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.autoharness.cartool.stuballowlist;

import android.annotation.NonNull;
import android.content.pm.SignedPackage;
import android.os.Bundle;
import android.os.allowlist.AllowlistManager;
import android.os.allowlist.AllowlistProviderService;
import android.os.allowlist.AllowlistRequest;
import android.os.allowlist.AllowlistResponse;
import android.os.allowlist.SignedPackageMultiMap;
import android.util.ArrayMap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Stub implementation of AllowlistProviderService to authorize appfunctions.
 * This service is mainly intended for debugging.
 */
public class CarToolForgeStubAllowlistService extends AllowlistProviderService {
    private static final String TAG = "CarToolForgeAllowlist";

    @Override
    @NonNull
    public AllowlistResponse onQueryAllowlist(@NonNull AllowlistRequest request) {
        Log.i(TAG, "onQueryAllowlist: request=" + request);

        int allowlistId = request.getAllowlistId();
        Bundle responseData = new Bundle();

        if (allowlistId == AllowlistManager.ALLOWLIST_ID_APP_FUNCTION) {
            ArrayList<SignedPackage> filterPackages = request.getData().getParcelableArrayList(
                    AllowlistManager.REQUEST_KEY_FILTER_PACKAGES, SignedPackage.class);
            ArrayList<SignedPackage> filterTargets = request.getData().getParcelableArrayList(
                    AllowlistManager.REQUEST_KEY_FILTER_TARGETS, SignedPackage.class);

            Log.i(TAG, "AppFunction query - FILTER_PACKAGES: " + filterPackages);
            Log.i(TAG, "AppFunction query - FILTER_TARGETS: " + filterTargets);

            // Allow all agent applications to invoke any appfunctions.
            Map<SignedPackage, List<SignedPackage>> allowedAgentsAndTargets = new ArrayMap<>();
            if (filterPackages != null) {
                for (SignedPackage agent : filterPackages) {
                    List<SignedPackage> allowedTargets = new ArrayList<>();
                    allowedTargets.add(new SignedPackage("*", null));
                    allowedAgentsAndTargets.put(agent, allowedTargets);
                }
            }

            responseData.putParcelable(
                    AllowlistManager.RESPONSE_KEY_ALLOWED_PACKAGE_MULTI_MAP,
                    new SignedPackageMultiMap(allowedAgentsAndTargets));

        } else {
            Log.w(TAG, "Unhandled allowlist ID: " + allowlistId);
        }

        return new AllowlistResponse(AllowlistManager.RESPONSE_STATUS_SUCCESS, responseData);
    }
}
