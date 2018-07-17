package HelperServices;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GETMoviesApiHelper
{
	//verify  the count query passed matched thes records returned
	public static boolean iscountcorrect(int sizeofrecordsreturned,int count, int sizeoflist) {
		
		if(count>0 && count<sizeoflist && sizeofrecordsreturned==count) {
			return true;
		}
		else if ((count==0 || count<0 ||count>sizeoflist) && sizeofrecordsreturned==sizeoflist){
			return true;
		}
		else {
			return false;
		}
	}
	
	//verify duplicate poster path links
	public static  boolean checkDuplicateUsingSet(List<String> listofposerpaths){
        Set<String> inputSet = new HashSet<String>(listofposerpaths);
        if(inputSet.size()< listofposerpaths.size()) {
            return false;
        }
        return true;
    }
	
	//get the duplicate poster path url list
	public static  Set<String> duplicateslist(List<String> listofposerpaths){
	 List<String> inputSet = new ArrayList<String>();
     Set<String> setToReturn = new HashSet<String>(); 
     for (String posterpathlinks : listofposerpaths)
     	{
    	 	if (inputSet.contains(posterpathlinks))
    	 		{
    	 				setToReturn.add(posterpathlinks);
    	 				
    	 		}
    	 	else {
    	 		inputSet.add(posterpathlinks);
    	 		 }
     	}
     return setToReturn;
     
	}
	
	//get the count of movies having genreids sum>400
	public static int countofmovies(List<List<Integer>> list_genre_ids) {
		int countofmovies=0;
		for (List<Integer> elementlist : list_genre_ids) {
			if( elementlist != null && !elementlist.isEmpty()) {
				//System.out.println(sumofgenre_ids(elementlist));
				if(sumofgenre_ids(elementlist)>400) {
					countofmovies+=1;
				}
			}
		}
		return countofmovies;
	}
	
	//verify the count of movies having sum of genre ids>400 are 7 or less than it
	public static boolean verifyiscountmovies(Integer countofmovies) {
		if (countofmovies>7){
			return false;
			}
		else {
			return true;
			}
			
	}
	
	//verify number of movie shaving palindrome word in its title
	public static boolean verifypalindromemoviescount(List<String> list_title) {
		
		for (String titleofmovie : list_title) { 
			if(isPalindromewords(titleofmovie)){
				return true;
			}
		}
		return false;
	}
	
	//verify number of movies having title of another movies in its title name
	public static boolean verifycontainstitlemoviescount(List<String> list_title) {
		int countcontainsanothermovietitle=0;
		
		for (String titleofmovie : list_title) { 
			if (list_title.contains(titleofmovie)) {
					if (countcontainsanothermovietitle>=2) {
						return true;
					}
					else {
						countcontainsanothermovietitle+=1;
					}
			}
		}
		return false;
	}
	
	//verify word is plaindrome
	public static boolean isPalindromewords(String str) {
		String [] arrofwords=str.split(" ");
		for (String words:arrofwords) {
			StringBuilder word = new StringBuilder();
		    word.append(words);
		    word = word.reverse();
		    if(word.toString().equals(words)) {
		       	return true;
		       }
		}
		return false;
	}
		
	//sum of genre ids
	public static int sumofgenre_ids(List<Integer> genre_ids) {
		int sum = 0;
		for(Integer d : genre_ids)
		   sum += d;
		return sum;
	}
    
	//verify sorting order
	public static boolean verifysort(List<List<Integer>>list_genre_ids,List<Integer> ids) {
		int countofnullgenreids=findnullgenreid(list_genre_ids);
		
		if(list_genre_ids.size()==ids.size()) {
			if(countofnullgenreids!=0) {
				List<List<Integer>> nullgenreidlist = list_genre_ids.subList(0,countofnullgenreids+1 );
				List<Integer> nullids = ids.subList(0,countofnullgenreids+1 );
				boolean resfornullgenreid=verifynullgenreids(nullgenreidlist,nullids);
				if(resfornullgenreid) {
					List<Integer> notnullids = ids.subList(countofnullgenreids+1,list_genre_ids.size());
					boolean resfornotnullgenreid=verifynotnullgenreids(notnullids);
					if(!resfornotnullgenreid) {
						System.out.println("Failing in sorting ids in ascending order for not null genre ids");
						return false;
					}
					else {
						return true;
					}
				}
				else {
					System.out.println("Failing in sorting null genre ids");
					return false;
				}
			}
			else {
				boolean resfornotnullgenreid=verifynotnullgenreids(ids);
				if(!resfornotnullgenreid) {
					System.out.println("Failing in sorting ids in ascending order for not null genre ids lists completely");
					return false;
				}
				else {
					return true;
				}
			}
		}
		else {
			System.out.println("we dont have genre id and ids attribut for all the records");
				return false;
			}
	}
	
	//finding null genre ids first
	public static int findnullgenreid(List<List<Integer>>list_genre_ids){
		int countofemptygenreids=0;
		for (List<Integer> elementlist : list_genre_ids) {
			if (elementlist.isEmpty()) {
				countofemptygenreids+=1;
			}
		}
		System.out.println("Count of null genre ids "+countofemptygenreids);
		return countofemptygenreids;
	}
	
	//verify null genre ids are first in the list and if so verify arranged by ascendin order of ids if more than 1
	public static boolean verifynullgenreids(List<List<Integer>> nullgenreidlist,List<Integer> nullids) {
		int i=0;
		boolean flag=true;
		
		while(i<nullgenreidlist.size()) {
			
			if (!nullgenreidlist.get(i).isEmpty()) {
				
				System.out.println("Failing in sorting null genre ids first");
				flag=false;
				break;
				}
			i+=1;
			}
		if(!flag) {
			return false;
		}
		else {
			boolean resfornullgenreid=sortids(nullids);
			if(!resfornullgenreid) {
				System.out.println("Failing in sorting ids in ascending order for null genre ids");
				return false;
			}
			return true;
		}
		
	}
	
	//verify non null genre ids are order by ids in ascending order
	public static boolean verifynotnullgenreids(List<Integer> notnullids) {
		boolean resfornotnullgenreid=sortids(notnullids);
		if(!resfornotnullgenreid) {
			System.out.println("Failing in sorting ids in ascending order for not null genre ids");
			return false;
		}
		return true;
	}
	
	//sorting function
	public static boolean sortids(List<Integer>ids){
		List<Integer> tmp = new ArrayList<Integer>(ids);
		Collections.sort(tmp);
		boolean sorted = tmp.equals(ids);
		return sorted;
		
	}
	
	//verify an object is a string
	public static boolean isString(List<Object> posterpath) {
		for(Object listitem:posterpath) {
			if (listitem==null) {
				return true;
			}
			if(listitem instanceof String) {
				return true;
			}
		}
		return false;
	}
		
	
}



	
	


