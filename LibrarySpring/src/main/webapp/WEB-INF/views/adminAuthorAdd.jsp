<div>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">Add New Author</h4>
	</div>
	<form action="addAuthor" method="post">
		<div class="modal-body">
			<p>Enter the name of author:</p>
			<input class="form-control" type="text" name="authorName"
				required="required"> <br />
			<!-- Put shite here -->

		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			<button type="submit" class="btn btn-primary">Create</button>
		</div>
	</form>
</div>