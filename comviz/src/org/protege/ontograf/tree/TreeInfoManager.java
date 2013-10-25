package org.protege.ontograf.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLEntity;

import uk.ac.manchester.cs.bhig.util.MutableTree;
import uk.ac.manchester.cs.bhig.util.Tree;
import ca.uvic.cs.chisel.cajun.graph.arc.GraphArc;
import ca.uvic.cs.chisel.cajun.graph.node.DefaultGraphNode;
import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;

public class TreeInfoManager {
        
        private static TreeInfoManager instance;
        
        
        private static Map<Object, EntityTreeInfo> entityTreeInfoMap;
        
        private static List<MutableTree> branchNodes;
        
        public static List<MutableTree> getBranchNodes() {
			return branchNodes;
		}

		private static boolean initiated = false;

        
        private TreeInfoManager(){
        };
        
        public static TreeInfoManager getTreeManager(){
        		if(instance == null){
                        instance = new TreeInfoManager();
                        entityTreeInfoMap = new HashMap();
        		}
                return instance;
        }
        
        public EntityTreeInfo getEntityTreeInfo(Object key){
        	return entityTreeInfoMap.get(key);
        }
        
        public Object getBranchEntity(Object key){
        	EntityTreeInfo entityTreeInfo  = entityTreeInfoMap.get(key);
        	
        	return entityTreeInfo.getBranchObject();
        }
        
        public Tree getTree(Object key){
        	return entityTreeInfoMap.get(key).getTreeNode();
        }
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public void generateTreeInfo(Collection nodes){
                
                List<GraphNode> localNodeList = new ArrayList();
                localNodeList.addAll(nodes);
                
                //ç”Ÿæˆ�ä¸€ä¸ªtreeList
                List<MutableTree> treeNodeList = new ArrayList();
                for(GraphNode node: localNodeList){
                        treeNodeList.add(new MutableTree(node));
                }
                
                for(int i = 0; i< treeNodeList.size(); i++){
                        for(int j = 1; j< treeNodeList.size(); j++){
                                if(i == j){
                                        continue;
                                }
                                DefaultGraphNode iNode = (DefaultGraphNode) treeNodeList.get(i).getUserObject();
                                DefaultGraphNode jNode = (DefaultGraphNode) treeNodeList.get(j).getUserObject();
                                
                                for(GraphArc arc: jNode.getArcs()){
                                        if(arc.getSource() == iNode){// source is parent
                                                if(!treeNodeList.get(i).getChildren().contains(treeNodeList.get(j)))
                                                        treeNodeList.get(i).addChild(treeNodeList.get(j));
                                                continue;
                                        }else if(arc.getDestination() == iNode){
                                                treeNodeList.get(i).setParent(treeNodeList.get(j));
                                                continue;
                                        }
                                }
                                
                        }
                }
                
                //now the treeList is formed
                
                //now we generate tree info for each entity
                
                //first, we find roots
                
                List<MutableTree> rootList = new ArrayList();
                
                for(MutableTree tree: treeNodeList){
                        if(tree.getParent() == null){
                                rootList.add(tree);
                        }
                }
                
                branchNodes = null;
                
                List<MutableTree> level1Nodes;

                if(rootList.size() == 1){
                        
                        branchNodes = rootList.get(0).getChildren();
                        //branchNodes.add(rootList.get(0));//only one root, the root have to have color
                        OWLEntity entity = (OWLEntity)(((GraphNode) (rootList.get(0).getUserObject())).getUserObject());
                        EntityTreeInfo entityTreeInfo = new EntityTreeInfo(entity, rootList.get(0), (OWLEntity)((GraphNode) rootList.get(0).getUserObject()).getUserObject(), 0, -1);
                        entityTreeInfoMap.put(entity, entityTreeInfo);

                }
                else{
                        branchNodes = rootList;
                }
                
                //then, we generate level and branch info for each node, map them into map
                
                //test
                Collection path = rootList.get(0).getPathToRoot();
               
                for(MutableTree t: treeNodeList){
                        
                        OWLEntity entity = (OWLEntity)(((GraphNode) (t.getUserObject())).getUserObject());
                        int level = -1;
                        
                        level = Math.max(t.getPathToRoot().size(), level);
                        
                        for(MutableTree branchTreeNode: branchNodes){
                                if(t.getPathToRoot().contains(branchTreeNode)){//that means they belong to the same cluster
                                        
                                        EntityTreeInfo entityTreeInfo = new EntityTreeInfo(entity, t, (OWLEntity)((GraphNode) branchTreeNode.getUserObject()).getUserObject(), level, -1);
                                        entityTreeInfoMap.put(entity, entityTreeInfo);
                                }
                        }
                        
                }
                
                this.initiated = true;
                
                return;
                
        }
        
        
        

}