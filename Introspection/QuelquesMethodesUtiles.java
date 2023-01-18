package edu.uha.miage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Yvan Maillot (yvan.maillot@uha.fr)
 */
public class ReflectUtil {

    /**
     * 1.01 Méthode qui retourne un tableau des méthodes d'une classe donnée qui
     * sont
     * <ul>
     * <li>public</li>
     * <li>static</li>
     * <li>dont tous les paramètres ainsi que le retour sont de type primitif ou String</li>
     *
     * </ul>
     * Par exemple,
     * <p>
     * appliquée à java.awt.Color, cette méthode retourne un tableau qui
     * contient :<br>
     *
     * static int java.awt.Color.HSBtoRGB(float,float,float)
     *
     * </p>(une seule méthode satisfait les exigences)
     * <p>
     * appliquée à String, cette méthode retourne un tableau qui contient
     * <ul>
     * <li>public static java.lang.String java.lang.String.valueOf(int), </li>
     * <li>public static java.lang.String java.lang.String.valueOf(boolean),  </li>
     * <li>public static java.lang.String java.lang.String.valueOf(char), </li>
     * <li>public static java.lang.String java.lang.String.valueOf(double),  </li>
     * <li>public static java.lang.String java.lang.String.valueOf(float),  </li>
     * <li>public static java.lang.String java.lang.String.valueOf(long) </li>
     * </ul>
     * </p>
     *
     * @param c la classe dont on veut des méthodes
     * @return le tableau des méthodes déclarées dans c qui respectent les
     * contraintes énoncées ci-dessus.
     */
    public static ArrayList<Method> arrayOfPublicStaticAndSoOnDeclaredMethods(Class<?> c) {
    	
       // return null;
    	ArrayList<Method> methods = new ArrayList<>();
    	
    	for(Method method: c.getDeclaredMethods()) {
    		if(goodMethod(method)) {
    			methods.add(method);
    		}
    	}
    	
    	
    	
    	
    	
    /*	
        for (Method m : c.getDeclaredMethods()) // On a toutes les méthodes de la classe c public ou pas 
        {
            if (Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(m.getModifiers())) {
            	
                boolean ok = true;
                for (Class<?> p : m.getParameterTypes()) {
                    if (!p.isPrimitive() && !p.equals(String.class)) {
                        ok = false;
                        break;
                    }
                }
                if (ok && (m.getReturnType().isPrimitive() || m.getReturnType().equals(String.class))) {
                    methods.add(m);
                }
            }
        }*/
        return methods;
    // TODO 1.01
    }
    

    private static boolean goodMethod(Method method) {
		// TODO Auto-generated method stub
    	int modifiers = method.getModifiers();
    	if(!Modifier.isPublic(modifiers) )
    			return false;
    	if(!Modifier.isStatic(modifiers))
    		return false;
    	if (!method.getReturnType().isPrimitive() && !method.getReturnType().equals(String.class))
    		
    		return false;
    	for(Class<?> param: method.getParameterTypes()) {
    		if(!param.isPrimitive() && param != String.class )
    			return false;
    	}
    		
		return true;
	}

	/**
     * 1.02 Méthode qui retourne un Set de tous les types auxquels appartient
     * l'objet passé en paramètre.
     * <p>
     * Par exemple allTypesOf("") retourne [class java.lang.Object, interface
     * java.lang.constant.Constable, interface java.lang.CharSequence, interface
     * java.lang.Comparable, interface java.io.Serializable, class
     * java.lang.String, interface java.lang.constant.ConstantDesc]
     * </p>
     * <ul>
     * <li>car "" est de type java.lang.String (c'est évident) </li>
     * <li>mais aussi de type java.lang.Object (puisque String hérite de Object)
     * </li>
     * <li>mais encore de type java.lang.CharSequence (puisque String implémente
     * CharSequence)</li>
     * <li>etc.</li>
     * </ul>
     * <p>
     * Il faut donc mettre dans cet ensemble, le type de o, mais aussi tous ses
     * super-types, mais aussi toutes leurs interfaces, mais toutes les
     * interfaces qu'elles étendent.
     * </p>
     *
     * @param o l'objet dont on veut les types
     * @return le Set de types
     */
    public static Set<Class<?>> allTypesOf(Object o) {      
        // TODO 1.02
    	/*Set<Class<?>> types = new HashSet<>();
    	Class<?> c = o.getClass();
    	types.add(c);
    	types.addAll(Arrays.asList(c.getInterfaces()));
    	while(c.getSuperclass() != null) {
    		c = c.getSuperclass();
    		types.add(c);
    		types.addAll(Arrays.asList(c.getInterfaces()));
    	}
    	return types;*/
    	var allTypes = new HashSet<Class<?>>();
        allTypesOf(o.getClass(), allTypes);
        return allTypes;
    	
    }
    private static void allTypesOf(Class<?> c, HashSet<Class<?>> allTypes) {
        if (c != null) {
            allTypes.add(c);
            allTypesOf(c.getSuperclass(), allTypes);
            for (var sc : c.getInterfaces()) {
                allTypesOf(sc, allTypes);
            }
        }
    }
        
    /**
     * 1.03 Méthode qui met à zéro tous les attributs (privés ou non) d'un
     * objet.Mettre à zéro signifie, plus précisément, donner
     * <ol>
     * <li>aux attributs numériques (int, long, double, ...) la valeur 0</li>
     * <li>aux attributs booléean la valeur false</li>
     * <li>aux attributs char la valeur '\u0000'</li>
     * <li>aux autres la valeur null.</li>
     * </ol>
     *
     * @param o à mettre à zéro
     * @throws java.lang.IllegalAccessException
     */
    public static void maz(Object o) throws IllegalAccessException {
    // TODO 1.03
        	Class<?> c = o.getClass();
        	try {
				
			
        	for(Field f: c.getDeclaredFields()) // pour le attribut privé et pas 
        		{
        		f.setAccessible(true); // permet de modifier les champs privées 
        		if(f.getType().isPrimitive()) {
        			if(f.getType() == int.class) {
        				f.setInt(o, 0);
        			}
        			else if(f.getType() == long.class) {
        				f.setLong(o, 0); // pour tester si 
        			}
        			else if(f.getType() == double.class) {
        				f.setDouble(o, 0);
        			}
        			else if(f.getType() == float.class) {
        				f.setFloat(o, 0);
        			}
        			else if(f.getType() == boolean.class) {
        				f.setBoolean(o, false);
        			}
        			else if(f.getType() == char.class) {
        				f.setChar(o, '\u0000');
        			}
        			else if(f.getType() == short.class) {
        				f.setShort(o, (short) 0);
        			}
        			else if(f.getType() == byte.class) {
        				f.setByte(o, (byte) 0);
        				
        			}
        		}
        		else {
        			f.set(o, null);
        		}
        	}
        	} catch (IllegalAccessException e) {
				// TODO: handle exception
			}
    }

    /**
     * 1.04 Méthode qui retourne la liste de toutes les super-classes de la
     * classe dont l'objet est passé en paramètre.
     * <p>
     * La liste des super-classes de java.lang.Object est [java.lang.Object]
     * car, par convention, la première super classe d'une classe est elle-même.
     * </p>
     * <p>
     * La liste des super-class de java.lang.String est [java.lang.String,
     * java.lang.Object]
     * </p>
     * <p>
     * La liste des super-class de java.util.ArrayList est [java.util.ArrayList,
     * java.util.AbstractList, java.util.AbstractCollection, java.lang.Object]
     * (dans cet ordre)
     * </p>
     * <p>
     * Par ailleurs, la liste des super-classes d'un type primitive (comme
     * int.class) est vide.
     * </p>
     * <p>
     * Enfin, null est retournée si le paramètre c est null.
     * </p>
     *
     * @param c un objet de classe
     * @return la liste des super-classes de c
     */
    public static List<Class<?>> getSuperClasses(Class<?> c)
    {
        if(c == null) return null;

        LinkedList<Class<?>> list = new LinkedList<>();

        if(c.isPrimitive()) return list;

        while(c != null)
        {
            list.add(c);
            c = c.getSuperclass();
        }

        return list;
    // TODO 1.04
    }

    /**
     * 1.05 Méthode qui retourne la classe qui la plus proche parente commune
     * (pppc) à deux classes données.
     *
     * <p>
     * Exemples :
     * <ol>
     * <li>pppc(String.class, Double.class) == Object.class</li>
     * <li>pppc(String.class, String.class) == String.class</li>
     * <li>pppc(Integer.class, Double.class) == Number.class</li>
     * <li>pppc(Integer.class, Number.class) == Number.class</li>
     * </ol>
     * </p>
     * <p>
     * La méthode n'est pas définie pour si un paramètre est null ou de type
     * primitif.
     * </p>
     *
     * @param c1 un objet de classe
     * @param c2 un objet de classe
     * @return la plus proche classe parente commune à c1 et c2.
     */
    public static Class<?> pppc(Class<?> c1, Class<?> c2) {
    	List<Class<?>> supeClassC1 = getSuperClasses(c1);
    	List<Class<?>> supeClassC2 = getSuperClasses(c2);
    	
    	supeClassC1.retainAll(supeClassC2);
    	return supeClassC1.get(0);

    	

    // TODO 1.05
    }

    /**
     * 1.06 Méthode qui retourne la classe qui la plus proche parente commune
     * (pppc) aux classes de deux objets donnés.
     *
     * <p>
     * Exemples :
     * <ol>
     * <li>pppc("Une chaine", 3.14) == Object.class</li>
     * <li>pppc("Une chaine", "Une autre") == String.class</li>
     * <li>pppc(1, 1.0) == Number.class</li>
     * </ol>
     * </p>
     *
     * @param o1 un objet quelconque
     * @param o2 un objet quelconque
     * @return la plus proche classe parente commune aux classes de o1 et o2
     */
    public static Class<?> pppcoo(Object o1, Object o2) {
    	return pppc(o1.getClass(), o2.getClass());
    // TODO 1.06
    }

    public static void main(String[] args) throws IllegalAccessException {
        // Écrire ici vos propres tests
        System.out.println(getSuperClasses(java.util.ArrayList.class));
        System.out.println(arrayOfPublicStaticAndSoOnDeclaredMethods(String.class));

    	//ArrayList<Method> arrayOfPublicStaticAndSoOnDeclaredMethods() ;       
    }
}
