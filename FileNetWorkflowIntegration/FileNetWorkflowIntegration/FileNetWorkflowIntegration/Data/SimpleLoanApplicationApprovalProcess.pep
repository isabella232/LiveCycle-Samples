<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE WorkFlowDefinition SYSTEM "wfdef4.dtd">
<WorkFlowDefinition ApiVersion="4.0"
Origin="JavaAPI"
	Subject="&quot;Simple Loan Application Approval Process&quot;"
	Description="This workflow illustrates integration of IBM FileNet workflow with Adobe LiveCycle ES using a Simple Loan Application Approval process as an example."
	Name="SimpleLoanApplicationApprovalProcess"
	AuthorTool="Process Designer"
	versionAgnostic="false"
	validateUsingSchema="true">
	<Field
		Name="renderServiceName"
		Description="Adobe LiveCycle ES Render service to be called"
		ValueExpr="&quot;&quot;"
		Type="string"
		IsArray="false"
		MergeType="default"/>
	<Field
		Name="submitServiceName"
		Description="Adobe LiveCycle ES Submit service to be called"
		ValueExpr="&quot;&quot;"
		Type="string"
		IsArray="false"
		MergeType="default"/>
	<Field
		Name="repositoryName"
		Description="Repository Name"
		ValueExpr="&quot;&quot;"
		Type="string"
		IsArray="false"
		MergeType="default"/>
	<Field
		Name="xmlDataPathInRepository"
		Description="Location of xml relative to the repository"
		ValueExpr="&quot;&quot;"
		Type="string"
		IsArray="false"
		MergeType="default"/>
	<Field
		Name="xdpAndDataRelationshipType"
		Description="Relationship between xdp and xml data"
		ValueExpr="&quot;&quot;"
		Type="string"
		IsArray="false"
		MergeType="default"/>
	<Field
		Name="xdpPathInRepository"
		Description="Location of xdp relative to the repository"
		ValueExpr="&quot;&quot;"
		Type="string"
		IsArray="false"
		MergeType="default"/>
	<Field
		Name="xmlDataFileNamePrefix"
		Description="Prefix for the xml data file name"
		ValueExpr="&quot;&quot;"
		Type="string"
		IsArray="false"
		MergeType="default"/>
	<Field
		Name="archiveSubmittedDataFolderPath"
		Description="The folder path where submitted data will be stored"
		ValueExpr="&quot;&quot;"
		Type="string"
		IsArray="false"
		MergeType="default"/>
	<Map
		Name="Workflow"
		MaxStepId="3" >
		<Step
			StepId="0"
			Name="Initiate Loan Application Approval Process"
			Description="Initiates Loan Application Approval process"
			XCoordinate="103"
			YCoordinate="26"
			RequestedInterface="Launch HTML (FileNET)"
			JoinType="none"
			SplitType="or"
			CanReassign="true"
			CanViewStatus="true"
			CanViewHistory="false"
			IgnoreInvalidUsers="false">
<Parameter
	Name="xmlDataPathInRepository"
	Description="Location of xml relative to the repository"
	ValueExpr="xmlDataPathInRepository"
	Type="string"
	IsArray="false"
	Mode="inout"/>
<Parameter
	Name="archiveSubmittedDataFolderPath"
	Description="The folder path where submitted data will be stored"
	ValueExpr="archiveSubmittedDataFolderPath"
	Type="string"
	IsArray="false"
	Mode="inout"/>
			<Route
				SourceStepId="0"
				DestinationStepId="3"
				Name="Approval"/>
			<PostAssignments>
			<Assign LVal="archiveSubmittedDataFolderPath" RVal="&quot;/LiveCycleES/ConnectorForIBMFileNet/ArchiveSubmittedDataWithNR-FileNet/Data/&quot;" />
			</PostAssignments>
<ModelAttributes>
	<ModelAttribute
		Name="UI_StepType"
		Type="int"
		IsArray="false">
			<Value Val="1"/>
	</ModelAttribute>
</ModelAttributes>
		</Step>
		<Step
			StepId="3"
			Name="Review Submitted Loan Application"
			Description="The step presents the submitted Loan application to the Loan Officer/Bank Manager for approval"
			XCoordinate="325"
			YCoordinate="26"
			RequestedInterface="Adobe LiveCycle ES Step"
			QueueName="Inbox"
			JoinType="or"
			SplitType="or"
			CanReassign="true"
			CanViewStatus="true"
			CanViewHistory="false"
			IgnoreInvalidUsers="false">
			<PreAssignments>
			<Assign LVal="renderServiceName" RVal="&quot;Samples%20-%20FileNetWorkflowIntegration/Processes/FileNetWorkflowStepRenderService&quot;" />
			<Assign LVal="submitServiceName" RVal="&quot;Samples%20-%20FileNetWorkflowIntegration/Processes/FileNetWorkflowStepSubmitService&quot;" />
			<Assign LVal="repositoryName" RVal="&quot;Samples&quot;" />
			<Assign LVal="xdpAndDataRelationshipType" RVal="&quot;Related Items&quot;" />
			<Assign LVal="xdpPathInRepository" RVal="&quot;/LiveCycleES/ConnectorForIBMFileNet/ArchiveSubmittedDataWithNR-FileNet/Forms/SimpleLoanApplication.xdp&quot;" />
			<Assign LVal="xmlDataFileNamePrefix" RVal="&quot;Reviewed_SimpleLoanApplication&quot;" />
			<Assign LVal="archiveSubmittedDataFolderPath" RVal="&quot;/LiveCycleES/ConnectorForIBMFileNet/ArchiveSubmittedDataWithNR-FileNet/Data&quot;" />
			</PreAssignments>
			<Participant Val="&quot;Administrator&quot;" />
<Parameter
	Name="xmlDataPathInRepository"
	Description="Location of xml relative to the repository"
	ValueExpr="xmlDataPathInRepository"
	Type="string"
	IsArray="false"
	Mode="inout"/>
<Parameter
	Name="renderServiceName"
	Description="Adobe LiveCycle ES Render service to be called"
	ValueExpr="renderServiceName"
	Type="string"
	IsArray="false"
	Mode="inout"/>
<Parameter
	Name="repositoryName"
	Description="Repository Name"
	ValueExpr="repositoryName"
	Type="string"
	IsArray="false"
	Mode="inout"/>
<Parameter
	Name="submitServiceName"
	Description="Adobe LiveCycle ES Submit service to be called"
	ValueExpr="submitServiceName"
	Type="string"
	IsArray="false"
	Mode="inout"/>
<Parameter
	Name="xdpAndDataRelationshipType"
	Description="Relationship between xdp and xml data"
	ValueExpr="xdpAndDataRelationshipType"
	Type="string"
	IsArray="false"
	Mode="inout"/>
<Parameter
	Name="xdpPathInRepository"
	Description="Location of xdp relative to the repository"
	ValueExpr="xdpPathInRepository"
	Type="string"
	IsArray="false"
	Mode="inout"/>
<Parameter
	Name="xmlDataFileNamePrefix"
	Description="Prefix for the submitted/approved data file name"
	ValueExpr="xmlDataFileNamePrefix"
	Type="string"
	IsArray="false"
	Mode="inout"/>
<Parameter
	Name="archiveSubmittedDataFolderPath"
	Description="The folder path where submitted data will be stored."
	ValueExpr="archiveSubmittedDataFolderPath"
	Type="string"
	IsArray="false"
	Mode="inout"/>
<ModelAttributes>
	<ModelAttribute
		Name="UI_StepType"
		Type="int"
		IsArray="false">
			<Value Val="2"/>
	</ModelAttribute>
</ModelAttributes>
		</Step>
	</Map>
</WorkFlowDefinition>
