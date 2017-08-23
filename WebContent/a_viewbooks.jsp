<%@include file="include.html"%>
<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="com.gcit.lms.entity.Genre"%>
<%@page import="com.gcit.lms.entity.Publisher"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.gcit.lms.entity.Author"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdminService"%>



<%
	AdminService adminService = new AdminService();
	List<Book> books = new ArrayList<>();
	String searchS = request.getParameter("searchString");
	if (searchS == null){
		searchS = "";
	}
    Integer booksCount = adminService.getBooksCount(searchS);
	int pages = 0;
	if(booksCount%10> 0){
		pages = booksCount/10+1;
	}else{
		pages = booksCount/10;
	}
	
	Integer pageNo= (Integer) request.getAttribute("pageNo");
	if (pageNo == null){
		pageNo = 1;
	}
	
	if(request.getAttribute("books")!=null){
		books = (List<Book>)(request.getAttribute("books"));
	}else{
		books = adminService.getAllBooks(1, "")	; 
	}
	
	
%>

<script>
function searchAuthors(){
	$.ajax({
		url: "searchBooks",
		data:{
			searchString: $('#searchString').val(),
			pageNo: $('#pageNo').val(),
			pages: $('#pages').val()
		}
	}).done(function (data){
		console.log(data);
		$('#viewArea').html(data)
		//alert(data);
	})
}
</script>

<div class="container"> <!-- jumbotron -->
	<h3>Welcome to GCIT Library Management System</h3>
	<h6>Below is the list of all Books in our Library System.</h6>
	${message}
	<div>
		<input type="text" name="searchString" id="searchString"
			placeholder="Author Name" aria-describedby="basic-addon1"
			oninput="searchAuthors()" value=<%=searchS %>> 
			<input type="hidden" name="pageNo" id= "pageNo" value=<%=pageNo%>>
			<input type="hidden" name="pages" id= "pages" value=<%=pages%>>
			
	</div>
	<div id="viewArea">

	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			<%for(int i=1; i<=pages; i++){ 
				String search = request.getParameter("searchString");
				if (search == null) {
					  search = "";
					}
				%>
				<li><a href="pageBooks?pageNo=<%=i%>&searchString=<%=search%>"><%=i%></a></li>
			<%} %>
			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
		</ul>
	</nav>
	<table class="table">
		<tr>
			<th>No</th>
			<th>Title</th>
			<th>Authors</th>
			<th>Genre</th>
			<th>Publisher</th>
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		<%
			for (Book b : books) {
		%>
		<tr>
			<td><%=books.indexOf(b) + 1%></td>
			<td><%=b.getTitle()%></td>
			<td>
				<%
					for (Author a : b.getAuthors()) {
							out.println("'"+a.getAuthorName() + "'");
						}
				%>
			</td>
			<td>
				<%
					for (Genre g : b.getGenres()) {
							out.println("'"+g.getGenreName() + "'");
						}
				%>
			</td>
			<td>
				<% if (b.getPublisher()!=null) {
						out.println(b.getPublisher().getPublisherName());
					}else{
						out.println("");
					}
				%>
			</td>
			<td><button type="button" class="btn btn-sm btn-primary"
					data-toggle="modal" data-target="#editBookModal"
					href="a_editbook.jsp?bookId=<%=b.getBookId()%>">Edit!</button></td>
			<td><button type="button" class="btn btn-sm btn-danger"
					onclick="javascript:location.href='deleteBook?bookId=<%=b.getBookId()%>'">Delete!</button></td>
					
		</tr>
		<%
			}
		%>
	</table>
	</div>
</div>

<div class="modal fade bs-example-modal-lg" tabindex="-1"
	id="editBookModal" role="dialog" aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content"></div>
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