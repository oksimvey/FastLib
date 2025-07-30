package com.robson.fastlib.api.data.file;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class MeuObjeto {

    private final String nome;
    private final int idade;
    private final String[] parentes;

    public String getNome() {
        return nome;
    }

    public String[] getParentes() {
        return parentes;
    }

    public int getIdade() {
        return idade;
    }

    public MeuObjeto(MeuObjetoParams params) {
        this.nome = params.nome();
        this.idade = params.idade() + 2;
        this.parentes = params.parentes();
    }

    public record MeuObjetoParams(String nome, int idade, String... parentes){}

    public static class MeuRecordSerializer extends Serializer<MeuObjetoParams> {

        @Override
        public void write(Kryo kryo, Output output, MeuObjetoParams record) {
            output.writeString(record.nome);
            output.writeInt(record.idade);
            String[] parentes = record.parentes();
            output.writeInt(parentes.length);  // escreve o tamanho do array
            for (String p : parentes) {
                output.writeString(p);
            }
        }

        @Override
        public MeuObjetoParams read(Kryo kryo, Input input, Class<? extends MeuObjetoParams> type) {
            String nome = input.readString();
            int idade = input.readInt();

            int length = input.readInt();
            String[] parentes = new String[length];
            for (int i = 0; i < length; i++) {
                parentes[i] = input.readString();
            }

            return new MeuObjetoParams(nome, idade, parentes);
        }
    }
}