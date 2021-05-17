package com.sandiego.lecturascaudal.Class;

import androidx.annotation.NonNull;

public class Login {

    int IdUsuario=0;
    String CodigoEmpleado,Contraseña,Usuario,Nombre,FechaIngreso;

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getCodigoEmpleado() {
        return CodigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        CodigoEmpleado = codigoEmpleado;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFechaIngreso() {
        return FechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        FechaIngreso = fechaIngreso;
    }
}
