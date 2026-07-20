const documentsContainer = document.getElementById("documents");
const chatMessages = document.getElementById("chatMessages");
const sendBtn = document.getElementById("sendBtn");
const queryInput = document.getElementById("queryInput");

let documents = [];

window.onload = async () => {
    await loadDocuments();
};

async function loadDocuments() {

    try {

        const response = await fetch("/documents");

        documents = [];

        const data = await response.json();
        data.documents.forEach(documentData => {
            documents.push([documentData.documentName, documentData.documentId, documentData.section, documentData.creationDateTime]);
        });

        console.log(documents);

        renderDocuments();

    } catch (e) {
        console.log(e);
        documentsContainer.innerHTML = "Failed to load documents.";

    }

}

function renderDocuments() {

    documentsContainer.innerHTML = "";

    documents.forEach(doc => {

        const div = document.createElement("div");

        div.className = "document";

        if (doc.selected)
            div.classList.add("selected");

        div.innerHTML = `

            <h3>${doc.documentName}</h3>

            <p>${doc.creationDateTime}</p>

        `;

        div.onclick = () => {

            doc.selected = !doc.selected;

            renderDocuments();

        };

        documentsContainer.appendChild(div);

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

async function sendQuery() {

    const query = queryInput.value.trim();

    if (query === "")
        return;

    addMessage(query, "user");

    queryInput.value = "";

    const loading = addLoading();

    const selectedDocs = documents
        .filter(d => d.selected)
        .map(d => ({

            documentId: d.documentId,

        }));

    try {

        const response = await fetch("/query", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({

                documents: selectedDocs,

                query: query

            })

        });

        const data = await response.json();

        loading.remove();

        addMessage(data.response, "bot");

    } catch (e) {
        loading.remove();

        addMessage("Something went wrong.", "bot");

    }

}