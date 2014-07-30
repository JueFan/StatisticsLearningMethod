package org.juefan.decisiontree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	private String feature;
    private List<TreeNode> childTreeNode;
    private String targetFunValue;
    private String nodeName;
    
    public TreeNode(String nodeName){
        
        this.nodeName = nodeName;
        this.childTreeNode = new ArrayList<TreeNode>();
    }
    
    public TreeNode(){
        this.childTreeNode = new ArrayList<TreeNode>();
    }

    public void printTree(){
    	if(targetFunValue != null)
    		System.out.print("特征值: " + targetFunValue + "\t");
    	if(nodeName != null)
    		System.out.print("类型: " + nodeName + "\t");
    	System.out.println();
    	for(TreeNode treeNode: childTreeNode){
    		System.out.println("当前特征为：" + feature);
    		treeNode.printTree();
    	}
    }
    public String getAttributeValue() {
        return feature;
    }

    public void setAttributeValue(String attributeValue) {
        this.feature = attributeValue;
    }

    public List<TreeNode> getChildTreeNode() {
        return childTreeNode;
    }

    public void setChildTreeNode(List<TreeNode> childTreeNode) {
        this.childTreeNode = childTreeNode;
    }

    public String getTargetFunValue() {
        return targetFunValue;
    }

    public void setTargetFunValue(String targetFunValue) {
        this.targetFunValue = targetFunValue;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

}
