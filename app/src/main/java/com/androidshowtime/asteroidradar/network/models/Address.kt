package com.androidshowtime.network.models

data class Address (
        val line1: String? = null,
        val line2: String? = null,
        val city: String?= null,
        val state: String?=null,
        val zip: String? = null
) {
    fun toFormattedString(): String {
       /* var output = line1.plus("\n")
        if (!line2.isNullOrEmpty()) output = output.plus(line2).plus("\n")
        output = output.plus("$city, $state $zip")
        return output*/


var formattedAddress = ""

        if (zip != null){

            if (state != null){

                if (city != null){

                    if ( line1 != null){

                        if (line2 != null){

                            formattedAddress = formattedAddress.plus("$line2, ")
                        }

                        formattedAddress = formattedAddress.plus("$line1, ")
                    }

                    formattedAddress = formattedAddress.plus("$city, ")
                }

                formattedAddress = formattedAddress.plus("$state, ")
            }

          formattedAddress = formattedAddress.plus("$zip")
        }

else{

   formattedAddress = ""
}

      return formattedAddress
    }
}