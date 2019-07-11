package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private List<Team> teams;
	
	//inserire tipo di dao

		private SerieADAO dao;

		

		//scelta valore mappa

		private Map<Integer,Season> idMap;

		

		//scelta tipo valori lista

		private List<Season> vertex;

		

		//scelta tra uno dei due edges

		private List<Adiacenza> edges;

		

		//scelta tipo vertici e tipo archi

		private DefaultDirectedWeightedGraph<Season, DefaultWeightedEdge> graph;
		
		private Map<Season, Integer> sp;

		

		public Model() {

			

			//inserire tipo dao

			dao  = new SerieADAO();

			//inserire tipo values

			idMap = new HashMap<Integer,Season>();

		}

		

		public String creaGrafo() {

			

			//scelta tipo vertici e archi

			graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

			

			//scelta tipo valori lista

			

			Graphs.addAllVertices(graph,vertex);

			

			edges = new ArrayList<Adiacenza>();
			
			for (Season s: sp.keySet())
			{
				for (Season t: sp.keySet())
				{
					if(s.getSeason()!=t.getSeason())
					{
						if (sp.get(s)>sp.get(t))
						{ double peso= sp.get(s)-sp.get(t);
							Adiacenza a= new Adiacenza(t.getSeason(), s.getSeason(), peso);
							edges.add(a);
						}
						if (sp.get(t)>sp.get(s))
						{ double peso= sp.get(t)-sp.get(s);
							Adiacenza a= new Adiacenza(s.getSeason(), t.getSeason(), peso);
							edges.add(a);
						}
					}
				}
			}

			

			for(Adiacenza a : edges) {

				

				//CASO BASE POTRESTI DOVER AGGIUNGERE CONTROLLI

				Season source = idMap.get(a.getId1());

				Season target = idMap.get(a.getId2());

				double peso = a.getPeso();

				Graphs.addEdge(graph,source,target,peso);

				System.out.println("AGGIUNTO ARCO TRA: "+source.toString()+" e "+target.toString());

				

			}

			

			System.out.println("#vertici: "+graph.vertexSet().size());

			System.out.println("#archi: "+graph.edgeSet().size());

			String ris= annataOro();
			
			return ris;
			
			
			

		}
	

	private String annataOro() {
			
		Season best=null;
		int peso=0;
		
		for (Season s: sp.keySet())
		{
			int temp= trovaPesoNuovo(s);
			if (temp>peso)
			{
				peso=temp;
				best= s;
			}
		
		}
		
		String ris= "L'annata migliore è " + best.getSeason() + " con peso "+ peso + ".\n";
		
			return ris;
		}



	private int trovaPesoNuovo(Season s) {
		
		int in=0;
		int out=0;
		
		for (DefaultWeightedEdge e: graph.incomingEdgesOf(s))
		{
			in+= graph.getEdgeWeight(e);
		}
		
		for (DefaultWeightedEdge e: graph.outgoingEdgesOf(s))
		{
			out+= graph.getEdgeWeight(e);
		}
		
		
		return (in-out);
	}



	public List<Team> getTeams() {
		
		teams= dao.listTeams();
		
		return teams;
	}



	public String classifica(Team t) {
		
		vertex = new ArrayList<Season>(dao.getVertex(t, idMap));
		sp= new LinkedHashMap<Season,Integer>();
		
		String ris= "Classifica: \n";
		
		for (Season s: vertex)
		{
			int peso= dao.getPunteggio(t, s);
			sp.put(s, peso);
			
			ris+= s.getDescription()+ " "+ peso+ "\n";
		}
		return ris;
		
	}
	
	

}
