const documentsContainer = document.getElementById("documents");
const chatMessages = document.getElementById("chatMessages");
const sendBtn = document.getElementById("sendBtn");
const queryInput = document.getElementById("queryInput");

window.onload = async () => {
    await loadDocuments();
};


//-----------------------------------------------------
// Sample Data
//-----------------------------------------------------

let documents = [];
let documentsToDelete = [];
/*{
    documentName:"Physics Notes",
    documentId:1,
    section:"Science",
    creationDateTime:"2026-07-20"
},
{
    documentName:"Chemistry",
    documentId:2,
    section:"Science",
    creationDateTime:"2026-07-19"
},
{
    documentName:"Algebra",
    documentId:3,
    section:"Math",
    creationDateTime:"2026-07-18"
},
{
    documentName:"Geometry",
    documentId:4,
    section:"Math",
    creationDateTime:"2026-07-17"
},
{
    documentName:"History",
    documentId:5,
    section:"Social",
    creationDateTime:"2026-07-16"
}
];
*/


let grouped = {};

function renderDocuments() {
    grouped = {};
//--------------------------------------------------
// Group by section
//--------------------------------------------------

    documents.forEach(doc => {
        if (!grouped[doc.section])
            grouped[doc.section] = [];

        doc.selected = false;
        grouped[doc.section].push(doc);
    });

    const container = document.getElementById("documentContainer");


//--------------------------------------------------
// Build UI
//--------------------------------------------------

    Object.entries(grouped).forEach(([sectionName, docs]) => {

        const section = document.createElement("div");
        section.className = "section";

        //--------------------------------------------------
        // Header
        //--------------------------------------------------

        const header = document.createElement("div");
        header.className = "section-header";

        const arrow = document.createElement("span");
        arrow.className = "arrow";
        arrow.textContent = "▶";

        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";

        const title = document.createElement("span");
        title.className = "section-title";
        title.textContent = sectionName;

        const action = document.createElement("div");
        action.className = "section-actions";

        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "Delete";

        action.appendChild(deleteBtn);

        header.appendChild(arrow);
        header.appendChild(checkbox);
        header.appendChild(title);
        header.appendChild(action);

        //--------------------------------------------------
        // Document list
        //--------------------------------------------------

        const list = document.createElement("div");
        list.className = "document-list";

        //--------------------------------------------------
        // Expand collapse
        //--------------------------------------------------

        header.addEventListener("click", e => {

            if (
                e.target === checkbox ||
                e.target === deleteBtn
            )
                return;

            if (list.style.display === "block") {
                list.style.display = "none";
                arrow.textContent = "▶";
            } else {
                list.style.display = "block";
                arrow.textContent = "▼";
            }

        });

        //--------------------------------------------------
        // Section checkbox
        //--------------------------------------------------

        checkbox.addEventListener("change", () => {

            docs.forEach(doc => {

                doc.selected = checkbox.checked;

                const c = document.getElementById(
                    "doc-" + doc.documentId
                );

                c.checked = checkbox.checked;

            });

            checkbox.indeterminate = false;

        });

        //--------------------------------------------------
        // Section delete
        //--------------------------------------------------

        deleteBtn.addEventListener("click", (e) => {

            e.stopPropagation();

            documentsToDelete = [];

            docs.filter(d => d.selected).forEach(doc => {
                documentsToDelete.push(doc)
            });

            openDeleteModal();

        });

        //--------------------------------------------------
        // Documents
        //--------------------------------------------------

        docs.forEach(doc => {

            const row = document.createElement("div");
            row.className = "document";

            const cb = document.createElement("input");
            cb.type = "checkbox";
            cb.id = "doc-" + doc.documentId;

            const name = document.createElement("div");
            name.className = "document-name";
            name.textContent = doc.documentName;

            const info = document.createElement("div");
            info.className = "document-info";
            info.textContent =
                doc.creationDateTime;

            const del = document.createElement("button");
            del.textContent = "Delete";

            //--------------------------------------------------
            // Checkbox
            //--------------------------------------------------

            cb.addEventListener("change", () => {

                doc.selected = cb.checked;

                updateSectionCheckbox(
                    checkbox,
                    docs
                );

            });

            //--------------------------------------------------
            // Delete
            //--------------------------------------------------

            del.addEventListener("click", () => {

                deleteDocument(doc);

            });

            row.appendChild(cb);
            row.appendChild(name);
            row.appendChild(info);
            row.appendChild(del);

            list.appendChild(row);

        });

        section.appendChild(header);
        section.appendChild(list);

        container.appendChild(section);

    });
}

//--------------------------------------------------
// Update section checkbox
//--------------------------------------------------

function updateSectionCheckbox(
    checkbox,
    docs
) {

    const selected = docs.filter(d => d.selected).length;

    if (selected === 0) {

        checkbox.checked = false;
        checkbox.indeterminate = false;

    } else if (selected === docs.length) {

        checkbox.checked = true;
        checkbox.indeterminate = false;

    } else {

        checkbox.checked = false;
        checkbox.indeterminate = true;

    }

}


//--------------------------------------------------
// Delete callbacks
//--------------------------------------------------

function deleteDocument(doc) {

    console.log("Delete document:", doc);

}


//--------------------------------------------------
// Selected documents
//--------------------------------------------------

function getSelectedDocuments() {

    return documents.filter(
        d => d.selected
    );

}


//--------------------------------------------------
// Unselected documents
//--------------------------------------------------

function getUnselectedDocuments() {

    return documents.filter(
        d => !d.selected
    );

}


function addDocument(documentData) {
    documents.unshift(documentData);
}

async function loadDocuments() {

    try {

        const response = await fetch("/documents");

        documents = [];

        const data = await response.json();
        data.documents.forEach(documentData => addDocument(documentData));

        console.log(documents);

        renderDocuments();

    } catch (e) {
        console.log(e);
        documentsContainer.innerHTML = "Failed to load documents.";

    }

}


function openDeleteModal() {

    const list = document.getElementById("deleteDocumentList");
    list.innerHTML = "";

    documentsToDelete.forEach(doc => {

        const row = document.createElement("div");
        row.className = "document-item";

        row.innerHTML = `
            <span>${doc.documentName}</span>
            <span>${doc.creationDateTime}</span>
        `;

        list.appendChild(row);

    });

    document.getElementById("deleteModal").style.display = "flex";

}

function closeDeleteModal() {

    documentsToDelete = [];

    document.getElementById("deleteModal").style.display = "none";

}

function confirmDelete() {

    deleteDocuments(documentsToDelete);

    closeDeleteModal();

}

function deleteDocuments(documentList) {

    console.log("Deleting...");

    documentList.forEach(doc => {
        console.log(doc.documentId);
    });

}


function addMessage(text, cls) {

    const div = document.createElement("div");

    div.className = "message " + cls;

    div.textContent = text;

    chatMessages.appendChild(div);

    chatMessages.scrollTop = chatMessages.scrollHeight;

    return div;

}

function addLoading() {

    const div = document.createElement("div");

    div.className = "message bot";

    div.innerHTML = `

        <div class="loading">
           <p>Querying....</p>
        </div>

    `;

    chatMessages.appendChild(div);

    chatMessages.scrollTop = chatMessages.scrollHeight;

    return div;

}

sendBtn.onclick = sendQuery;

queryInput.addEventListener("keydown", (e) => {

    if (e.key === "Enter" && !e.shiftKey) {

        e.preventDefault();

        sendQuery();

    }

});


function parseOutput(text) {
    // Parse JSON escapes if wrapped in quotes
    if (text.startsWith('"') && text.endsWith('"')) {
        text = JSON.parse(text);
    }

    // Remove opening code fence (``` or ```java)
    text = text.replace(/^```[a-zA-Z0-9_-]*\n?/, "");

    // Remove closing code fence
    text = text.replace(/\n?```$/, "");

    return text;
}


async function sendQuery() {

    const query = queryInput.value.trim();

    if (query === "")
        return;

    addMessage(query, "user");

    queryInput.value = "";

    const loading = addLoading();

    const selectedDocIds = getSelectedDocuments().map(d => d.documentId);

    try {
        console.log(selectedDocIds)

        const response = await fetch("/query", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({

                allowedDocuments: selectedDocIds,

                query: query

            })

        });

        const data = await response.json();

        loading.remove();
        console.log(data.response);

        addMessage(parseOutput(data.response), "bot");

    } catch (e) {
        loading.remove();

        addMessage("Something went wrong.", "bot");

    }

}


// Files upload handler

const modal = document.getElementById("uploadModal");
const openModalBtn = document.getElementById("openModal");
const selectFileBtn = document.getElementById("selectFileBtn");
const fileInput = document.getElementById("fileInput");
const uploadBtn = document.getElementById("uploadBtn");
const cancelBtn = document.getElementById("cancelBtn");
const fileName = document.getElementById("fileName");
const status = document.getElementById("status");

let selectedFiles = [];

// Open popup
openModalBtn.addEventListener("click", () => {
    modal.style.display = "block";
    status.textContent = "";
});

// Open file chooser
selectFileBtn.addEventListener("click", () => {
    fileInput.click();
});

// Store selected files
fileInput.addEventListener("change", () => {

    selectedFiles = Array.from(fileInput.files);

    if (selectedFiles.length === 0) {
        fileName.textContent = "No file selected";
        return;
    }

    fileName.innerHTML = selectedFiles
        .map(file => `• ${file.name}`)
        .join("<br>");
});

// Upload files
uploadBtn.addEventListener("click", async () => {

    if (selectedFiles.length === 0) {
        alert("Please select at least one file.");
        return;
    }

    const sectionName = document.getElementById("section-name").value;

    if (sectionName.trim() === "") {
        document.getElementById("showSectionStatus").innerHTML = `
            <p style="color: red">Invalid section name!</p>
            `;
        return;
    }

    document.getElementById("showSectionStatus").innerHTML = "";

    const formData = new FormData();

    // Add every file
    selectedFiles.forEach(file => {
        formData.append("files", file);
    });

    try {

        status.style.color = "blue";
        status.textContent = "Uploading...";

        const response = await fetch("/upload/".concat(sectionName), {
            method: "POST",
            body: formData
        });

        if (!response.ok) {
            throw new Error("Upload failed");
        }

        const result = await response.json();

        status.style.color = "green";
        status.textContent = `File(s) uploaded successfully.`;
        console.log(result);

        result.addedDocuments.forEach(documentData => addDocument(documentData));

        renderDocuments();

        setTimeout(resetModal, 1000);

    } catch (err) {
        console.error(err);
        status.style.color = "red";
        status.textContent = "Upload failed.";
    }
});

// Cancel
cancelBtn.addEventListener("click", resetModal);

// Reset popup
function resetModal() {
    modal.style.display = "none";
    fileInput.value = "";
    selectedFiles = [];
    fileName.textContent = "No file selected";
    status.textContent = "";
}