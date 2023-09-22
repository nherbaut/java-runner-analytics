async function getMySnippets(auth_token) {
    let mySnippets = undefined;
    const response = await fetch("https://code-snippet.miage.dev/snippet/mine",
        {
            method: 'GET',
            headers: {"Authorization": "Bearer " + auth_token}
        });
    if (!response.ok) {
        throw new Error("failed to retreive my snippets")
    }
    return JSON.parse(await response.json());
}

export {getMySnippets}