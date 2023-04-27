package com.example.teleappsistencia.ui.fragments.usuarios_sistema;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.servicios.APIService;
import com.example.teleappsistencia.servicios.ClienteRetrofit;
import com.example.teleappsistencia.ui.fragments.opciones_listas.OpcionesListaFragment;
import com.example.teleappsistencia.ui.fragments.paciente.PacienteAdapter;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsuariosSistemaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsuariosSistemaFragment extends Fragment implements OpcionesListaFragment.OnButtonClickListener, UsuarioSistemaAdapter.OnItemSelectedListener {
    // ! REFEREENCIAS GUI
    private Button btnNewUser;
    private RecyclerView recycler;

    // Otras Variables
    private OpcionesListaFragment frag_acciones;
    private UsuarioSistemaAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    static List<Usuario> lUsuarios;
    private int selectedPosition = RecyclerView.NO_POSITION;

    // Constructor público vacío requerido por newInstance()
    public UsuariosSistemaFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UsuariosSistemaFragment.
     */
    public static UsuariosSistemaFragment newInstance() {
        UsuariosSistemaFragment fragment = new UsuariosSistemaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    /**
     * Cuando se infle la vista del fragment, insertamos el OpcionesListaFragment, conectamos los eventos
     * de los botones y los listeners, configuramos el RecyclerView y mostramos el Shimmer
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuarios_sistema, container, false);
        lUsuarios = new ArrayList<>();

        // Extraer referencias GUI
        btnNewUser = view.findViewById(R.id.btnNewUser);
        recycler   = view.findViewById(R.id.lv_recyclerView);
        frag_acciones = new OpcionesListaFragment();

        // Cargar fragment de acciones
        getActivity().getSupportFragmentManager().beginTransaction()
            .add(R.id.fragView_botonesAcciones, frag_acciones)
            .commit();

        // Configurar acciones
        btnNewUser.setOnClickListener(v -> cargarFragmentNuevoUsuario());
        frag_acciones.setOnButtonClickListener(this);

        // Configurar el RecyclerView
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        // Mostrar el Shimmer mientras cargan los datos
        // ConstraintLayout layoutContenido = view.findViewById(R.id.layoutContenido);
        // Utilidad.generarCapaEspera(view, layoutContenido);

        // Cargar datos en el RecyclerView
        cargarUsuarios(view);

        return view;
    }

    /**
     * Método para obtener una lista de usuarios de la API para luego mostrarlos en un RecyclerView
     *
     * @param view Vista
     */
    private void cargarUsuarios(View view) {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();

        Call<List<Usuario>> call = apiService.getUsuarios(Constantes.BEARER + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    lUsuarios = response.body();

                    // Crear Adaptador para el RecyclerView
                    adapter = new UsuarioSistemaAdapter(lUsuarios);
                    recycler.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), Constantes.ERROR_AL_LISTAR_LAS_DIRECCIONES, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
            }
        });
    }

    private void cargarFragmentNuevoUsuario() {
        // TODO
    }

    /**
     * Recarga el fragment padre para mostrar los nuevos contenidos del RecyclerView.
     */
    private void recargarFragment() {
//        MainActivity activity = (MainActivity) this.context;
//        UsuariosSistemaFragment usuarios_fragment = new UsuariosSistemaFragment();
//        activity.getSupportFragmentManager()
//            .beginTransaction()
//            .replace(R.id.main_fragment, usuarios_fragment)
//            .addToBackStack(null).commit();
    }

    // ! Métodos del UsuarioSistemaAdapter
    @Override
    public void onItemSelected(int position) {
        selectedPosition = position;
    }

    // ! Acciones del OpcionesListaFragment
    @Override
    public void onViewDetailsButtonClicked() {
        // TODO
    }

    @Override
    public void onDeleteButtonClicked() {
        // TODO
    }

    @Override
    public void onEditButtonClicked() {
        // TODO
    }
}