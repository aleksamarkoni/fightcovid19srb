package com.covidvolonter.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.covidvolonter.di.Injectable
import com.covidvolonter.main.fragments.MapType.*
import com.covidvolonter.util.MAP_TYPE
import com.covidvolonter.util.TinyDb
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.fightcorona.R
import kotlinx.android.synthetic.main.fragment_choose_map_type.*
import javax.inject.Inject


class ChooseMapTypeFragment : BottomSheetDialogFragment(), Injectable {

    @Inject
    lateinit var tinyDb: TinyDb

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_map_type, container, false)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupChosenMap()

        default_map.setOnClickListener {
            tinyDb.putString(MAP_TYPE, DEFAULT_MAP.name)
            setupSelectedUi(true, false, false)
            setupMapForFragment(DEFAULT_MAP)
        }

        satellite_map.setOnClickListener {
            tinyDb.putString(MAP_TYPE, SATELLITE_MAP.name)
            setupSelectedUi(false, true, false)
            setupMapForFragment(SATELLITE_MAP)
        }

        terrain_map.setOnClickListener {
            tinyDb.putString(MAP_TYPE, TERRAIN_MAP.name)
            setupSelectedUi(false, false, true)
            setupMapForFragment(TERRAIN_MAP)
        }
    }

    private fun setupChosenMap() {
        val chosenMapType = tinyDb.getString(MAP_TYPE, SATELLITE_MAP.name)
        val chooseMap = valueOf(chosenMapType!!)

        when (chooseMap) {
            DEFAULT_MAP -> {
                setupSelectedUi(
                    defaultSelected = true,
                    sateliteSelected = false,
                    terrainSelected = false
                )
            }
            SATELLITE_MAP -> {
                setupSelectedUi(
                    defaultSelected = false,
                    sateliteSelected = true,
                    terrainSelected = false
                )
            }
            TERRAIN_MAP -> {
                setupSelectedUi(
                    defaultSelected = false,
                    sateliteSelected = false,
                    terrainSelected = true
                )
            }
        }
    }

    private fun setupSelectedUi(
        defaultSelected: Boolean,
        sateliteSelected: Boolean,
        terrainSelected: Boolean
    ) {
        default_text_map.isSelected = defaultSelected
        default_map.isSelected = defaultSelected

        satellite_text_map.isSelected = sateliteSelected
        satellite_map.isSelected = sateliteSelected

        terrain_text_map.isSelected = terrainSelected
        terrain_map.isSelected = terrainSelected
    }

    private fun setupMapForFragment(maptype: MapType) {
        val fm = requireActivity().supportFragmentManager
        val fragment = fm.findFragmentById(R.id.nav_host_fragment_activity_measurement_history)
        fragment?.let {
            try {
                val mapFragment =
                    fragment.childFragmentManager.fragments.first() as MapFragment
                mapFragment.setMapType(maptype)
                //dismiss()

            } catch (e: ClassCastException) {

            }
        }
    }
}

enum class MapType {
    DEFAULT_MAP,
    SATELLITE_MAP,
    TERRAIN_MAP
}