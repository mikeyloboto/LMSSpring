<%@include file="include.html"%>

${message}
<script>
	function searchBorrower(page) {

		$.ajax({
			url : "searchBorrower",
			data : {
				searchString : $('#searchString').val(),
				pageNo : page
			}
		}).done(function(data) {
			//alert(data);
			var arr_data = String(data).split("\n");
			$('#tableBorrower').html(arr_data[0]);
			$('#pagination').html(arr_data[1]);
			
		})
	}
</script>
<ol class="breadcrumb">
	<li><a href="index">Home</a></li>
	<li><a href="admin">Administrator</a></li>
	<li class="active">Borrower Management</li>
</ol>
<div class="container">
	<div>
		<button type="button" class="btn btn-success" data-toggle="modal"
			data-target="#borrowerModal" href="adminBorrowerAdd">Add
			New Borrower</button>
		<div class="page-header">
			<h1>List of Existing Borrowers in LMS</h1>
		</div>
		<form action="searchPublishers">
			<input type="text" class="form-control" name="searchString"
				id="searchString" placeholder="Search" oninput="searchBorrower(1)">
		</form>
		<nav aria-label="Page navigation">
			<ul class="pagination" id="pagination">
				
		
			</ul>
		</nav>
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Card Number</th>
					<th>Borrower Name</th>
					<th>Address</th>
					<th>Phone</th>
					<th>Actions</th>
					<!-- <th>Delete</th> -->
				</tr>
			</thead>
			<tbody id="tableBorrower">
				
			</tbody>
		</table>
	</div>

	<div class="modal fade bs-example-modal-lg" id="borrowerModal"
		tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">....</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		// codes works on all bootstrap modal windows in application
		$('.modal').on('hidden.bs.modal', function(e) {
			$(this).removeData();
		});

	});
</script>
<script>
	$(document).ready ( function(){
		searchBorrower(1);
	});
</script>