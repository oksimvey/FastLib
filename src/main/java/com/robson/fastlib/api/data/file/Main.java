package com.robson.fastlib.api.data.file;

import java.nio.file.Path;

public class Main {

    static Path path = Path.of("src/main/resources/dados");

    static Path to = Path.of("src/main/resources/new");

    static Path scan = to.resolve("dados.fastdata");


    public static void main(String[] args) {
       read();
    }

    static void write(){
       try {
           DataSerializer.convertJsonDirectoryToFastdata(path, to, MeuObjeto.MeuObjetoParams.class);
       }
       catch (Exception e) {

       }
    }

    private static void read() {
        System.out.println("running");
        try {
            MeuObjeto.MeuObjetoParams objeto1 = DataSerializer.fromFastdata(scan, MeuObjeto.MeuObjetoParams.class);
            // Acesso correto aos campos, usando getters se estiver usando record
            MeuObjeto objeto = new MeuObjeto(objeto1);
            System.out.println("Nome: " + objeto.getNome() + " Idade: " + objeto.getIdade());
            for (String parente : objeto.getParentes()){
                System.out.println( parente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
