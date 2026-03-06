package tests;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Textos {
    private Textos() {}

    /** Normaliza para comparar: quita acentos, normaliza espacios, quita chars raros, lower-case. */
    public static String normalizar(String s) {
        if (s == null) return "";

        // Saltos de línea consistentes
        String t = s.replace("\r\n", "\n").replace("\r", "\n");

        // Quitar BOM
        t = t.replace("\uFEFF", "");

        // Unicode -> separa diacríticos
        t = Normalizer.normalize(t, Normalizer.Form.NFD);

        // Quitar diacríticos (acentos)
        t = t.replaceAll("\\p{M}+", "");

        // Espacios raros a espacio normal
        t = t.replace('\u00A0', ' ')
                .replace('\u2007', ' ')
                .replace('\u202F', ' ');

        // Tabs a espacio
        t = t.replace("\t", " ");

        // Quitar control chars (dejando \n)
        t = t.replaceAll("[\\p{Cntrl}&&[^\n]]", "");

        // Colapsar espacios múltiples
        t = t.replaceAll(" +", " ");

        // Lowercase estable
        t = t.toLowerCase(Locale.ROOT);

        return t.trim();
    }

    /** Devuelve tokens tipo "identificador" (letras, números, _) del texto ya normalizado o no. */
    public static List<String> extraerIdentificadores(String texto) {
        String t = normalizar(texto);
        List<String> out = new ArrayList<>();
        // Identificadores Java simples
        var m = java.util.regex.Pattern.compile("\\b[a-z_][a-z0-9_]*\\b").matcher(t);
        while (m.find()) out.add(m.group());
        return out;
    }

    /** Distancia Levenshtein (0 = iguales). */
    public static int distanciaLevenshtein(String a, String b) {
        String s1 = normalizar(a);
        String s2 = normalizar(b);

        int n = s1.length();
        int m = s2.length();
        if (n == 0) return m;
        if (m == 0) return n;

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            char c1 = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char c2 = s2.charAt(j - 1);
                int cost = (c1 == c2) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[m];
    }

    /** Encuentra el candidato más parecido a 'objetivo' dentro de 'candidatos'. */
    public static String masParecido(String objetivo, List<String> candidatos) {
        if (candidatos == null || candidatos.isEmpty()) return null;
        String obj = normalizar(objetivo);

        String best = null;
        int bestDist = Integer.MAX_VALUE;

        for (String c : candidatos) {
            int d = distanciaLevenshtein(obj, c);
            if (d < bestDist) {
                bestDist = d;
                best = c;
            }
        }
        return best;
    }
    public static String diffContextual(String esperado, String real, int contexto) {
        if (esperado == null) esperado = "";
        if (real == null) real = "";

        String[] eLines = normalizar(esperado).split("\n");
        String[] rLines = normalizar(real).split("\n");

        int max = Math.max(eLines.length, rLines.length);
        int idx = -1;

        // Buscar primera línea distinta
        for (int i = 0; i < max; i++) {
            String e = (i < eLines.length) ? eLines[i] : "<fin>";
            String r = (i < rLines.length) ? rLines[i] : "<fin>";
            if (!e.equals(r)) {
                idx = i;
                break;
            }
        }

        if (idx == -1) {
            return "No differences found.";
        }

        int from = Math.max(0, idx - contexto);
        int to = Math.min(max - 1, idx + contexto);

        StringBuilder sb = new StringBuilder();
        sb.append("=== DIFERENCIA EN LÍNEA ").append(idx + 1).append(" ===\n");

        for (int i = from; i <= to; i++) {
            String e = (i < eLines.length) ? eLines[i] : "<fin>";
            String r = (i < rLines.length) ? rLines[i] : "<fin>";

            if (i == idx) {
                sb.append(">> ");
            } else {
                sb.append("   ");
            }

            sb.append(String.format("%4d | Esperado: %s%n", i + 1, e));
            sb.append(String.format("     | Obtenido: %s%n", r));
        }

        return sb.toString();
    }
    public static String diffDetallado(
            String esperadoRaw,
            String obtenidoRaw,
            int contextoLineas,
            int numCaracteres
    ) {
        if (esperadoRaw == null) esperadoRaw = "";
        if (obtenidoRaw == null) obtenidoRaw = "";

        // Comparamos sobre normalizado (robusto)
        String esperado = normalizar(esperadoRaw);
        String obtenido = normalizar(obtenidoRaw);

        String[] eLines = esperado.split("\n", -1);
        String[] oLines = obtenido.split("\n", -1);

        int maxLines = Math.max(eLines.length, oLines.length);

        // 1) Encontrar primera línea distinta
        int lineaDiff = -1;
        for (int i = 0; i < maxLines; i++) {
            String e = (i < eLines.length) ? eLines[i] : "";
            String o = (i < oLines.length) ? oLines[i] : "";
            if (!e.equals(o)) {
                lineaDiff = i;
                break;
            }
        }

        StringBuilder sb = new StringBuilder();

        // 2) Contexto de líneas alrededor del fallo
        if (lineaDiff >= 0) {
            int from = Math.max(0, lineaDiff - Math.max(0, contextoLineas));
            int to = Math.min(maxLines - 1, lineaDiff + Math.max(0, contextoLineas));

            sb.append("=== PRIMERA DIFERENCIA EN LÍNEA ").append(lineaDiff + 1).append(" ===\n");
            sb.append("=== CONTEXTO (").append(contextoLineas).append(" líneas antes/después) ===\n");
            for (int i = from; i <= to; i++) {
                String e = (i < eLines.length) ? eLines[i] : "<fin>";
                String o = (i < oLines.length) ? oLines[i] : "<fin>";
                sb.append(i == lineaDiff ? ">> " : "   ");
                sb.append(String.format("%4d | Esperado: %s%n", i + 1, e));
                sb.append("   ");
                sb.append(String.format("%4d | Obtenido: %s%n", i + 1, o));
            }
            sb.append("\n");

            // 3) Además: ventana de caracteres de la línea que falla, centrada en el primer char distinto
            String eLine = (lineaDiff < eLines.length) ? eLines[lineaDiff] : "";
            String oLine = (lineaDiff < oLines.length) ? oLines[lineaDiff] : "";

            int charDiff = primeraDiferenciaChar(eLine, oLine);

            sb.append("=== DIFERENCIA EN LA LÍNEA ").append(lineaDiff + 1)
                    .append(" (primer carácter distinto en posición ").append(charDiff + 1).append(") ===\n");

            sb.append("Esperado (ventana): ").append(recorteAlrededor(eLine, charDiff, numCaracteres)).append("\n");
            sb.append("Obtenido (ventana): ").append(recorteAlrededor(oLine, charDiff, numCaracteres)).append("\n");
        } else {
            sb.append("No se encontraron diferencias (tras normalizar).\n\n");
        }

        // 4) Dump completo: primero Esperado, luego Obtenido, con números de línea
        sb.append("=== ESPERADO (normalizado) ===\n");
        sb.append(conNumerosDeLinea(eLines));
        sb.append("\n=== OBTENIDO (normalizado) ===\n");
        sb.append(conNumerosDeLinea(oLines));
        sb.append("\n");

        return sb.toString();
    }

    private static int primeraDiferenciaChar(String a, String b) {
        int n = Math.min(a.length(), b.length());
        for (int i = 0; i < n; i++) {
            if (a.charAt(i) != b.charAt(i)) return i;
        }
        // Si todos los min chars son iguales, difieren por longitud (o son iguales)
        return n;
    }

    /**
     * Recorta una línea mostrando una ventana de 'numCaracteres' centrada alrededor de 'pos'.
     * Añade "[...]" a izquierda/derecha si se recorta.
     */
    private static String recorteAlrededor(String s, int pos, int numCaracteres) {
        if (numCaracteres <= 0) return s; // sin recorte
        if (s == null) s = "";

        if (s.length() <= numCaracteres) return s;

        // Queremos que 'pos' esté dentro de la ventana.
        // Si hay texto correcto antes, mostramos parte de ese contexto.
        int half = Math.max(1, numCaracteres / 2);

        int start = pos - half;
        int end = start + numCaracteres;

        if (start < 0) {
            start = 0;
            end = numCaracteres;
        }
        if (end > s.length()) {
            end = s.length();
            start = Math.max(0, end - numCaracteres);
        }

        boolean cutLeft = start > 0;
        boolean cutRight = end < s.length();

        String core = s.substring(start, end);
        if (cutLeft) core = "[...]" + core;
        if (cutRight) core = core + "[...]";
        return core;
    }


    private static String espacios(int n) {
        if (n <= 0) return "";
        return " ".repeat(n);
    }

    private static String conNumerosDeLinea(String[] lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            sb.append(String.format("%4d | %s%n", i + 1, lines[i]));
        }
        return sb.toString();
    }


}
