package com.floresv.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.floresv.campusgps.model.Posicion
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

/** Implementacion con FusedLocationProviderClient (Google Play Services). */
class UbicacionRepositoryGoogle(private val context: Context) : UbicacionRepository {

    private val cliente = LocationServices.getFusedLocationProviderClient(context)

    // El permiso se revisa en la pantalla antes de llamar a buscar()
    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> = try {
        val ubicacion = cliente
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .await()
        if (ubicacion != null) Result.success(Posicion(ubicacion.latitude, ubicacion.longitude))
        // getCurrentLocation devuelve null si no puede ubicarte (por ejemplo, con el GPS apagado)
        else Result.failure(UbicacionNoDisponibleException())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun playServicesDisponible(): Boolean =
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
}
