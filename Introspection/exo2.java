package edu.uha.miage;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Yvan Maillot (yvan.maillot@uha.fr)
 */
public class ReflectUtil {

    /**
     * 1.01 Méthode qui retourne vrai si l'objet de classe passé en paramètre
     * est de type primitif ou de type String, faux sinon.
     *
     * @param c un objet de classe
     * @return c est de type simple (primitif) ou de type java.util.String
     */
    public static boolean isPrimitiveOrString(Class<?> c) {
        return c.isPrimitive() || c.equals(String.class);
    }
    /**
     * 1.02 Méthode qui retourne vrai si la méthode passée en paramètre est une
     * méthode
     * <ol>
     * <li>publique </li>
     * <li>statique </li>
     * <li>dont les paramètres sont de types primitifs ou String et </li>
     * <li>le type de retour est de type primitif ou String ou absent
     * (void).</li>
     * </ol>
     *
     * @param m une méthode
     * @return m respecte les critères de la spécification
     * @see Modifier
     */
    public static boolean isSimpleMethod(Method m) {
        return Modifier.isPublic(m.getModifiers())
                && Modifier.isStatic(m.getModifiers())
                && isPrimitiveOrString(m.getReturnType())
                && Arrays.stream(m.getParameterTypes())
                        .allMatch(ReflectUtil::isPrimitiveOrString);
    }

    /**
     * 1.03 Méthode qui retourne la liste de toutes les méthodes qui respectent
     * le critère isSimpleMethod(m) qui sont directement déclarées dans la
     * classe dont l'objet est donnée en paramètre.
     *
     * @param c un objet de classe
     * @return la liste des méthodes statiques directement déclarées dans c
     */
    public static ArrayList<Method> getAllSimpleMethods(Class<?> c) {

        ArrayList<Method> methods = new ArrayList<>();
        for (Method m : c.getDeclaredMethods()) {
            if (isSimpleMethod(m)) {
                methods.add(m);
            }
        }
        return methods;
    }

    /**
     * 1.04 Méthode qui lit dans in (Scanner) une valeur du type c donné en
     * paramètre s'il est de type simple ou String.
     * <p>
     * La valeur saisie est retournée ou null si le type en entrée ne respecte
     * pas les contraintes.
     *
     * @param c le type de la valeur à saisir au clavier
     * @param in Scanner pour lire la valeur
     * @return la valeur saisie ou null
     */
    public static Object read(Class<?> c, Scanner in)
    {
        if (isPrimitiveOrString(c)) {
            PropertyEditor editor = PropertyEditorManager.findEditor(c);
            editor.setAsText(in.nextLine());
            return editor.getValue();
        }
        return null;
    }
    /**
     * 1.05 Méthode qui retourne un tableau de valeurs lu dans in (Scanner passé
     * en paramètre). Le tableau de valeurs retourné doit "coller" avec les
     * paramètres de la méthode passée en paramètre.
     *
     * @param method la méthode dont on veut un tableau de valeurs
     * @param in Scanner dans lequel lire les valeurs
     * @return le tableau de valeurs correspondant aux paramètres de method
     */
    public static Object[] readParams(Method method, Scanner in) {
        Class<?>[] params = method.getParameterTypes(); // les types des paramètres
        Object[] values = new Object[params.length]; // les valeurs des paramètres
        for (int i = 0; i < params.length; i++) {          
            System.out.print("Entrez un " + params[i].getName() + " : "); 
            values[i] = read(params[i], in); 
        } 
        return values;
    }

    /**
     *  1.06 Écrire un programme complet qui, en utilisant certaines des méthodes<br>
     *  précédentes et en écrivant éventuellement d'autres,
     *  <ol>
     *  <li>lit au clavier le nom pleinement qualifié d'une classe </li>
     *  <li>affiche sur la sortie standard la liste de toutes les méthodes de cette classe issues de getAllSimpleMethods() précédées d'un numéro d'ordre en commençant par 0 </li>
     *  <li>propose à l'utilisateur de choisir une méthode en saisisant au clavier un entier qui est son numéro d'ordre. </li>
     *  <li>demande au clavier des valeurs pour les paramètres de cette méthodes </li>
     *  <li>exécute la méthode et affiche son résultat.   </li>
     *  </ol>
     *
     *  Un exemple d'utilisation :<br>
     *  <p>
     *  Donnez le nom pleinement qualifié d'une classe : java.util.Date<br>
     *  ---------------------------------
     *  </p>
     *  <ul>
     *  <li>0. public static long java.util.Date.parse(java.lang.String)</li>
     *  <li>1. public static long java.util.Date.UTC(int,int,int,int,int,int)</li>
     *  </ul>
     *  <p>
     *  ---------------------------------<br>
     *  Choisir une méthode : 0<br>
     *  Entrez un java.lang.String : 14/12/2020<br>
     *  parse(14/12/2020) = 1613084400000<br>
     *  ---------------------------<br>
     *  </p>
     *  Un autre exemple :<br>
     *  Donnez le nom pleinement qualifié d'une classe : java.awt.Color<br>
     * <br>
     *  ---------------------------------<br>
     *  0. public static int java.awt.Color.HSBtoRGB(float,float,float)<br>
     *  ---------------------------------<br>
     *  Choisir une méthode : 0<br>
     *  Entrez un float : 0,5<br>
     *  Entrez un float : 0,1<br>
     *  Entrez un float : 0,3<br>
     *  HSBtoRGB(0.5, 0.1, 0.3) = -12235443<br>
     *  ---------------------------<br>
     */
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.print("Donnez le nom pleinement qualifié d'une classe : ");
        String className = in.nextLine();
        Class<?> c = Class.forName(className);
        ArrayList<Method> methods = getAllSimpleMethods(c);
        System.out.println("---------------------------------");
        for (int i = 0; i < methods.size(); i++) {
            System.out.println(i + ". " + methods.get(i));
        }
        System.out.println("---------------------------------");
        System.out.print("Choisir une méthode : ");
        int choice = in.nextInt();
        in.nextLine();
        Method method = methods.get(choice);
        Object[] params = readParams(method, in);
        Object result = method.invoke(null, params);
        System.out.println(method.getName() + "(" + Arrays.toString(params) + ") = " + result);
        System.out.println("---------------------------");
    }


    
}
